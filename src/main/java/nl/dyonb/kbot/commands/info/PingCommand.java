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
        // Absolutely cursed, No one should ever do this.
        String executedCommand = commandContext.getSplitMessage().get(0).toLowerCase();
        int index = this.getCommandInfo().getNames().indexOf(executedCommand);
        if (index == 1) index = 0;
        else if (index == 0) index = 1;
        String finalExecutedCommand = this.getCommandInfo().getNames().get(index);

        final long start = System.currentTimeMillis();
        commandContext.reply(finalExecutedCommand + "ing..").subscribe(message -> message.edit(
                        messageEditSpec -> messageEditSpec.setContent(finalExecutedCommand + "!\n" + (System.currentTimeMillis() - start) + "ms"))
                        .block());
    }
}
