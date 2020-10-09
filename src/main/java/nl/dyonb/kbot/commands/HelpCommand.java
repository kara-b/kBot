package nl.dyonb.kbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import nl.dyonb.kbot.CommandManager;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import nl.dyonb.kbot.util.kBotUtilities;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super(new CommandInfo.Builder(HelpCommand.class)
                .setDescription("Help me!")
                .setNames(List.of("help", "commands")));
    }

    @Override
    public void execute(CommandContext commandContext) {
        if (!commandContext.getArgs().isEmpty()) {
            BaseCommand baseCommand = CommandManager.getInstance().getCommand(commandContext.getArgs().get(0));

            if (baseCommand == null || !baseCommand.getCommandInfo().isEnabled()) {
                commandContext.replyBlocking("Command couldn't be found or isn't enabled!");
                return;
            }

            commandContext.replyEmbedBlocking(embedCreateSpec -> {
                embedCreateSpec.setTitle("Help for " + baseCommand.getCommandInfo().getNames().get(0))
                        .setDescription("**" + String.join(", ", baseCommand.getCommandInfo().getNames()) + "**\n"
                                + baseCommand.getCommandInfo().getDescription());
            });
            return;
        }

        // shit code
        Map<String, BaseCommand> commandMap = CommandManager.getInstance().getCommands();
        List<BaseCommand> baseCommandsDone = new ArrayList<>();
        List<String> nonDuplicateCommands = new ArrayList<>();
        commandMap.forEach((s, baseCommand) -> {
            if (!baseCommandsDone.contains(baseCommand)) {
                baseCommandsDone.add(baseCommand);
                nonDuplicateCommands.add(s);
            }
        });

        commandContext.replyEmbedBlocking(embedCreateSpec -> {
            embedCreateSpec.setDescription(String.join("\n", nonDuplicateCommands));
        });
    }
}
