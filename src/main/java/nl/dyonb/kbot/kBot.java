package nl.dyonb.kbot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.ApplicationInfoData;
import discord4j.discordjson.json.UserData;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import nl.dyonb.kbot.util.database.DatabaseManager;
import nl.dyonb.kbot.util.kBotConfig;
import nl.dyonb.kbot.util.listeners.EventListener;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

public class kBot {

    public static final Logger DEFAULT_LOGGER = Loggers.getLogger("kbot");
    private static GatewayDiscordClient gateway;
    public static kBotConfig config;
    public static Snowflake ownerId;

    public static void main(String[] args) {
        config = new kBotConfig("kbot.json5");

        DiscordClient discordClient = DiscordClientBuilder.create(config.token).build();

        discordClient.getApplicationInfo()
                .map(ApplicationInfoData::owner)
                .map(UserData::id)
                .map(Snowflake::of)
                .doOnNext(ownerId -> {
                    DEFAULT_LOGGER.info("Owner ID acquired: {}", ownerId);
                    kBot.ownerId = ownerId;
                })
                .block();

        discordClient.gateway()
                .setInitialStatus(shardInfo -> Presence.online(Activity.listening("people")))
                .setEnabledIntents(IntentSet.of(Intent.GUILD_MEMBERS,
                        Intent.GUILD_MESSAGES,
                        Intent.DIRECT_MESSAGES,
                        Intent.GUILDS))
                .withGateway(gatewayDiscordClient -> {
                    kBot.gateway = gatewayDiscordClient;

                    ScanResult scanResult = new ClassGraph().enableClassInfo().scan();
                    List<String> eventClassNames = scanResult.getClassesImplementing(EventListener.class.getCanonicalName()).getNames();
                    for (String eventClassName : eventClassNames) {
                        try {
                            Class clazz = Class.forName(eventClassName);
                            EventListener eventListener = (EventListener) clazz.getDeclaredConstructor().newInstance();
                            registerEvent(gateway, eventListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return kBot.gateway.onDisconnect();
                }).block();

        DatabaseManager.getInstance().close();
        System.exit(0);
    }

    private static <T extends Event> void registerEvent(GatewayDiscordClient gateway, EventListener<T> eventListener) {
        gateway.getEventDispatcher()
                .on(eventListener.getEventType())
                .flatMap(eventListener::execute)
                .subscribe();
    }

}
