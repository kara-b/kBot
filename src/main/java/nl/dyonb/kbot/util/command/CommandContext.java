package nl.dyonb.kbot.util.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import nl.dyonb.kbot.util.kBotUtilities;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CommandContext {

    private final MessageCreateEvent event;
    private final Message message;
    private MessageChannel channel;
    private Guild guild;
    private final List<String> splitMessage;
    private final List<String> args;
    private final Member invokerMember;
    private Member botMember;

    public CommandContext(MessageCreateEvent event) {
        this.event = event;
        message = event.getMessage();
        splitMessage = kBotUtilities.splitMessage(message);
        args = kBotUtilities.extractArguments(message);
        invokerMember = event.getMember().orElseThrow();
    }

    public Message getMessage() {
        return message;
    }

    public MessageChannel getChannel() {
        if (channel == null)
            channel = getMessage().getChannel().blockOptional().orElseThrow();
        return channel;
    }

    public Guild getGuild() {
        if (guild == null)
            guild = getMessage().getGuild().blockOptional().orElseThrow();
        return guild;
    }

    public List<String> getSplitMessage() {
        return splitMessage;
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public Member getInvokerMember() {
        return invokerMember;
    }

    public Member getBotMember() {
        if (botMember == null)
            botMember = getGuild().getMemberById(event.getClient().getSelfId()).blockOptional().orElseThrow();
        return botMember;
    }

    public Mono<Message> reply(String text) {
        return getChannel().createMessage(text);
    }

    public Message replyBlocking(String text) {
        return reply(text).block();
    }

    public Mono<Message> replyEmbed(Consumer<EmbedCreateSpec> consumer) {
        return getChannel().createEmbed(consumer);
    }

    public Message replyEmbedBlocking(Consumer<EmbedCreateSpec> consumer) {
        return replyEmbed(consumer).block();
    }
}
