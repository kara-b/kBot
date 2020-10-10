package nl.dyonb.kbot.commands.moderation;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import reactor.core.publisher.Flux;

import java.util.List;

public class PurgeCommand extends BaseCommand {
    public PurgeCommand() {
        super(new CommandInfo.Builder(PurgeCommand.class)
                .setDescription("Purges messages")
                .setNames(List.of("purge", "prune"))
                .setPermissions(PermissionSet.of(Permission.MANAGE_MESSAGES)));
    }

    @Override
    public void execute(CommandContext commandContext) {
        if (commandContext.getArgs().isEmpty()) {
            commandContext.replyBlocking("Please give the amount of messages to purge.");
            return;
        }

        int count;
        try {
            count = Integer.parseInt(commandContext.getArgs().get(0));
        } catch (NumberFormatException e) {
            commandContext.replyBlocking("Invalid number!");
            return;
        }

        Flux.just(commandContext.getMessage().getId())
                .flatMap(commandContext.getChannel()::getMessagesBefore)
                .take(count)
                .flatMap(Message::delete)
                .subscribe();
        commandContext.getMessage().delete().subscribe();
    }
}
