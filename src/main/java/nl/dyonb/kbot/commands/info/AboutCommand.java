package nl.dyonb.kbot.commands.info;

import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import nl.dyonb.kbot.util.kBotUtilities;

import java.util.List;

public class AboutCommand extends BaseCommand {
    public AboutCommand() {
        super(new CommandInfo.Builder(AboutCommand.class)
                .setDescription("Gives some info about the bot")
                .setNames(List.of("about", "info")));
    }

    @Override
    public void execute(CommandContext commandContext) {
        commandContext.replyEmbedBlocking(embedCreateSpec ->
                embedCreateSpec.setTitle("About kBot")
                        .setDescription("**Bot source**\n" +
                                kBot.config.bot_source + "\n" +
                                "**Support server**\n" +
                                kBot.config.supportServer + "\n")
                        .setThumbnail(commandContext.getBotMember().getAvatarUrl())
                        .setColor(kBotUtilities.randomBrightColor()));
    }
}
