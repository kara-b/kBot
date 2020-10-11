package nl.dyonb.kbot.commands.info;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.rest.util.Image;
import nl.dyonb.kbot.util.command.BaseCommand;
import nl.dyonb.kbot.util.command.CommandContext;
import nl.dyonb.kbot.util.command.CommandInfo;
import nl.dyonb.kbot.util.kBotUtilities;

import java.util.List;

public class ServerInfoCommand extends BaseCommand {
    public ServerInfoCommand() {
        super(new CommandInfo.Builder(ServerInfoCommand.class)
                .setDescription("Provides info about the server")
                .setNames(List.of("server", "serverinfo")));
    }

    @Override
    public void execute(CommandContext commandContext) {
        Guild guild = commandContext.getGuild();
        Member owner = guild.getOwner().block();

        List<GuildChannel> guildChannelList = guild.getChannels().collectList().block();
        long voiceChannels = guildChannelList.stream().filter(VoiceChannel.class::isInstance).count();
        long textChannels = guildChannelList.stream().filter(TextChannel.class::isInstance).count();

        List<Member> memberList = guild.getMembers().collectList().block();
        long botAmount = memberList.stream().filter(User::isBot).count();
        long humanAmount = memberList.stream().count() - botAmount;

        commandContext.replyEmbedBlocking(embedCreateSpec -> {
            embedCreateSpec.setTitle("Server info for " + guild.getName())
                    .setThumbnail(guild.getIconUrl(Image.Format.JPEG).orElse(""))
                    .addField("Owner", owner.getDisplayName() + "#" + owner.getDiscriminator(), true)
                    .addField("Region", guild.getRegion().block().getName(), true)
                    .addField("Channels", String.format("**Voice:** %d%n**Text:** %d", voiceChannels, textChannels), true)
                    .addField("Members", String.format("**Total:** %d%n**Humans:** %d%n**Bots:** %d", humanAmount+botAmount, humanAmount, botAmount), true)
                    .setFooter(String.format("ID: %s", guild.getId().asString()), "")
                    .setColor(kBotUtilities.randomBrightColor());
        });
    }
}
