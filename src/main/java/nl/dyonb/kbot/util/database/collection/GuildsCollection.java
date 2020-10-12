package nl.dyonb.kbot.util.database.collection;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoDatabase;
import discord4j.common.util.Snowflake;
import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.database.DatabaseManager;
import nl.dyonb.kbot.util.database.bean.DatabaseGuildBean;
import nl.dyonb.kbot.util.database.entity.DatabaseGuild;
import org.bson.Document;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class GuildsCollection extends DatabaseCollection {
    public GuildsCollection(MongoDatabase mongoDatabase) {
        super(mongoDatabase.getCollection(kBot.config.mongoGuildsName));
    }

    public Mono<DatabaseGuild> getDatabaseGuild(Snowflake guildId) {
        final Publisher<Document> request = this.getCollection()
                .find(Filters.eq("_id", guildId.asString()))
                .first();

        return Mono.from(request)
                .flatMap(document -> Mono.fromCallable(() -> new DatabaseGuild(DatabaseManager.objectMapper.readValue(document.toJson(), DatabaseGuildBean.class))))
                .doOnSuccess(consumer -> {
                    if (consumer == null) {
                        kBot.DEFAULT_LOGGER.debug("[DatabaseGuild {}] Not found", guildId.asLong());
                    }
                })
                .defaultIfEmpty(new DatabaseGuild(guildId));
    }
}
