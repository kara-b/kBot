package nl.dyonb.kbot.util.command;

public abstract class BaseCommand {

    private final CommandInfo commandInfo;

    protected BaseCommand(CommandInfo.Builder commandInfoBuilder) {
        this.commandInfo = commandInfoBuilder.createCommandInfo();
    }

    public abstract void execute(CommandContext commandContext);

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
