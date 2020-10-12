package nl.dyonb.kbot.util.database.entity;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import discord4j.common.util.Snowflake;
import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.database.DatabaseManager;
import nl.dyonb.kbot.util.database.bean.DatabaseGuildBean;
import nl.dyonb.kbot.util.database.entity.SerializableEntity;
import reactor.core.publisher.Mono;

public class DatabaseGuild extends SerializableEntity<DatabaseGuildBean> {
    public DatabaseGuild(DatabaseGuildBean databaseGuildBean) {
        super(databaseGuildBean);
    }

    public DatabaseGuild(Snowflake guildId) {
        super(new DatabaseGuildBean(guildId.asString()));
    }

    public Snowflake getId() {
        return Snowflake.of(this.getBean().getId());
    }

    public Mono<Void> insert() {
        kBot.DEFAULT_LOGGER.info("[DatabaseGuild {}] Inserting/Updating...",
                this.getId().asLong());

        return Mono.from(DatabaseManager.getGuilds()
                .getCollection()
                .replaceOne(Filters.eq("_id", this.getId().asString()), this.toDocument(), new ReplaceOptions().upsert(true)))
                .doOnNext(updateResult -> kBot.DEFAULT_LOGGER.info("[DatabaseGuild {}] Update/Insertion result: {}",
                        this.getId().asLong(), updateResult))
                .then();
    }

    public Mono<Void> delete() {
        return Mono.from(DatabaseManager.getGuilds()
                .getCollection()
                .deleteOne(Filters.eq("_id", this.getId().asString())))
                .doOnNext(result -> kBot.DEFAULT_LOGGER.info("[DatabaseGuild {}] Deletion result: {}", this.getId().asLong(), result))
                .then();
    }

    @Override
    public String toString() {
        return "DatabaseGuild{" +
                "bean=" + this.getBean() +
                '}';
    }
}
