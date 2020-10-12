package nl.dyonb.kbot.util.listeners;

import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.object.entity.User;
import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.database.entity.DatabaseGuild;
import nl.dyonb.kbot.util.database.DatabaseManager;
import reactor.core.publisher.Mono;

public class GuildDeleteListener implements EventListener<GuildDeleteEvent> {
    private static final String leaveText = String.format("You have removed me from your server, Thank you for testing me!" +
            "%nIf you encountered an issue or want to tell me what could be improved, Do not hesitate to join the support server and tell me!" +
            "%n%s", kBot.config.supportServer);

    @Override
    public Class<GuildDeleteEvent> getEventType() {
        return GuildDeleteEvent.class;
    }

    @Override
    public Mono<Void> execute(GuildDeleteEvent event) {
        if (!event.isUnavailable()) {
            Mono.from(event.getClient().getUserById(event.getGuild().get().getOwnerId()))
                    .flatMap(User::getPrivateChannel)
                    .flatMap(privateChannel -> privateChannel.createMessage(leaveText))
                    .subscribe();

            return DatabaseManager.getGuilds()
                    .getDatabaseGuild(event.getGuildId())
                    .flatMap(DatabaseGuild::delete);
        }

        return Mono.empty();
    }
}
