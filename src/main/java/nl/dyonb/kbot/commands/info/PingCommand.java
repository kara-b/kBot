package nl.dyonb.kbot.commands.info;

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
        final long start = System.currentTimeMillis();
        commandContext.reply("Pinging..").subscribe(message -> message.edit(messageEditSpec -> messageEditSpec.setContent("Pong!\n" + (System.currentTimeMillis() - start) + "ms")).block());
    }
}
