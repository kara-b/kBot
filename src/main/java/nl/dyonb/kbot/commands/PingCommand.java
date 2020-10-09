package nl.dyonb.kbot.commands;

import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;

import java.util.List;

public class PingCommand extends BaseCommand {
    public PingCommand() {
        super(new CommandInfo.Builder(PingCommand.class)
                .setDescription("Ping Pong!").setNames(List.of("ping", "pong")));
    }

    @Override
    public void execute(CommandContext commandContext) {
        commandContext.replyBlocking("Pong!");
    }
}
