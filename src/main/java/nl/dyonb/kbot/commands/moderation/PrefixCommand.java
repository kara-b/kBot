package nl.dyonb.kbot.commands.moderation;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import nl.dyonb.kbot.util.database.entity.DatabaseGuild;
import nl.dyonb.kbot.util.database.DatabaseManager;

import java.util.List;

public class PrefixCommand extends BaseCommand {
    public PrefixCommand() {
        super(new CommandInfo.Builder(PrefixCommand.class)
                .setNames(List.of("prefix"))
                .setDescription("Sets the command prefix for the current server")
                .setUserPermissions(PermissionSet.of(Permission.ADMINISTRATOR)));
    }

    @Override
    public void execute(CommandContext commandContext) {
        if (commandContext.getArgs().isEmpty()) {
            commandContext.replyBlocking("Please specify the prefix to use.");
            return;
        }
        String newPrefix = commandContext.getArgs().get(0);
        Message message = commandContext.replyBlocking(":hourglass: Setting prefix to " + newPrefix + "...");

        DatabaseGuild databaseGuild = DatabaseManager.getGuilds().getDatabaseGuild(commandContext.getGuild().getId()).block();
        databaseGuild.getBean().setPrefix(newPrefix);
        databaseGuild.insert().block();

        message.edit(messageEditSpec -> messageEditSpec.setContent(":white_check_mark: Set the prefix to " + newPrefix)).block();
    }
}
