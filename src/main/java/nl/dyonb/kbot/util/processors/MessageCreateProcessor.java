package nl.dyonb.kbot.util.processors;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandManager;
import nl.dyonb.kbot.util.database.entity.DatabaseGuild;
import nl.dyonb.kbot.util.database.DatabaseManager;
import nl.dyonb.kbot.util.kBotUtilities;
import reactor.core.publisher.Mono;

public class MessageCreateProcessor {

    public static Mono<Void> processMessage(MessageCreateEvent messageCreateEvent) {
        String prefix = kBot.config.defaultPrefix;

        DatabaseGuild databaseGuild = DatabaseManager.getGuilds().getDatabaseGuild(messageCreateEvent.getGuildId().get()).block();
        if (databaseGuild.getBean().getPrefix() != null) {
            prefix = databaseGuild.getBean().getPrefix();
        }

        Message message = messageCreateEvent.getMessage();

        if (message.getContent().startsWith(String.format("<@!%s> ", kBot.botId.asString()))) {
            prefix = String.format("<@!%s> ", kBot.botId.asString());
        }

        // If message doesn't start with prefix or message author is a bot
        if (!message.getContent().startsWith(prefix) || !message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
            return Mono.empty();
        }

        String commandName = kBotUtilities.splitMessage(message, prefix).get(0);
        BaseCommand command = CommandManager.getInstance().getCommand(commandName);
        CommandContext commandContext = new CommandContext(messageCreateEvent, prefix);

        if (command == null) {
            commandContext.replyBlocking("Command not found!");
        } else if (!command.getCommandInfo().isEnabled()) {
            commandContext.replyBlocking("Command isn't enabled!");
        } else if (!hasPermissions(command, commandContext)) {
            return Mono.empty();
        } else if (command.getCommandInfo().isBotOwnerOnly() && !commandContext.getInvokerMember().getId().equals(kBot.ownerId)) {
            return Mono.empty();
        } else {
            command.execute(commandContext);
        }
        return Mono.empty();
    }

    public static boolean hasPermissions(final BaseCommand baseCommand, final CommandContext commandContext) {
        PermissionSet invokerPerms = commandContext.getInvokerMember().getBasePermissions().block();

        if (!invokerPerms.contains(Permission.ADMINISTRATOR)) {
            for (Permission requires : baseCommand.getCommandInfo().getUserPermissions()) {
                if (!invokerPerms.contains(requires)) {
                    commandContext.replyBlocking("You don't have the permissions to do this! " + requires.name());
                    return false;
                }
            }
        }

        PermissionSet botPerms = commandContext.getBotMember().getBasePermissions().block();

        if (!botPerms.contains(Permission.ADMINISTRATOR)) {
            for (Permission requires : baseCommand.getCommandInfo().getBotPermissions()) {
                if (!botPerms.contains(requires)) {
                    commandContext.replyBlocking("The bot doesn't have permission to do this! " + requires.name());
                    return false;
                }
            }
        }

        return true;
    }
}
