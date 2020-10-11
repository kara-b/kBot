package nl.dyonb.kbot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.ApplicationInfoData;
import discord4j.discordjson.json.UserData;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import nl.dyonb.kbot.util.processors.MessageCreateProcessor;
import nl.dyonb.kbot.util.kBotConfig;
import reactor.util.Logger;
import reactor.util.Loggers;

public class kBot {

    public static String prefix = "k!";
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
                        Intent.DIRECT_MESSAGES))
                .withGateway(gatewayDiscordClient -> {
                    kBot.gateway = gatewayDiscordClient;

                    gateway.getEventDispatcher().on(MessageCreateEvent.class)
                            .subscribe(MessageCreateProcessor::processMessage);

                    return kBot.gateway.onDisconnect();
                }).block();

        System.exit(0);
    }

}
