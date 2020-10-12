package nl.dyonb.kbot.util.listeners;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import nl.dyonb.kbot.util.processors.MessageCreateProcessor;
import reactor.core.publisher.Mono;

public class MessageCreateListener implements EventListener<MessageCreateEvent> {
    @Override
    public Class getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent messageCreateEvent) {
        return MessageCreateProcessor.processMessage(messageCreateEvent);
    }
}
