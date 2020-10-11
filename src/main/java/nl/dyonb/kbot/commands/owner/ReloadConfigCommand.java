package nl.dyonb.kbot.commands.owner;

import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import nl.dyonb.kbot.util.kBotConfig;

import java.util.List;

public class ReloadConfigCommand extends BaseCommand {
    public ReloadConfigCommand() {
        super(new CommandInfo.Builder(ReloadConfigCommand.class)
                .setNames(List.of("reload_config", "config_reload"))
                .setDescription("Reloads the config")
                .setBotOwnerOnly(true));
    }

    @Override
    public void execute(CommandContext commandContext) {
        commandContext.reply(":hourglass: Reloading config...")
                .flatMap(message -> {
                    try {
                        kBot.config.configManager.load();
                        kBot.config.configManager.loadObject(kBot.config);

                        return message.edit(messageEditSpec -> messageEditSpec.setContent(":white_check_mark: Done reloading the config."));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return message.edit(messageEditSpec -> messageEditSpec.setContent(":x: Error reloading the config."));
                    }
                })
                .block();
    }
}
