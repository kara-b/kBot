package nl.dyonb.kbot.util.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import nl.dyonb.kbot.util.kBotUtilities;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CommandContext {

    private final Message message;
    private final MessageChannel channel;
    private final Guild guild;
    private final List<String> args;
    private final Member invokerMember;
    private final Member botMember;

    public CommandContext(MessageCreateEvent event) {
        message = event.getMessage();
        channel = message.getChannel().blockOptional().orElseThrow();
        guild = message.getGuild().blockOptional().orElseThrow();
        args = kBotUtilities.extractArguments(message);
        invokerMember = event.getMember().orElseThrow();
        botMember = guild.getMemberById(event.getClient().getSelfId()).blockOptional().orElseThrow();
    }

    public Message getMessage() {
        return message;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public Member getInvokerMember() {
        return invokerMember;
    }

    public Member getBotMember() {
        return botMember;
    }

    public Mono<Message> reply(String text) {
        return channel.createMessage(text);
    }

    public Message replyBlocking(String text) {
        return reply(text).block();
    }

    public Mono<Message> replyEmbed(Consumer<EmbedCreateSpec> consumer) {
        return channel.createEmbed(consumer);
    }

    public Message replyEmbedBlocking(Consumer<EmbedCreateSpec> consumer) {
        return replyEmbed(consumer).block();
    }
}
