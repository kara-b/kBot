package nl.dyonb.kbot.util.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import nl.dyonb.kbot.kBot;
import nl.dyonb.kbot.util.database.codec.SnowflakeCodec;
import nl.dyonb.kbot.util.database.collection.GuildsCollection;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class DatabaseManager {
    public static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    public static final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromCodecs(new SnowflakeCodec()));

    private static DatabaseManager instance;

    static {
        instance = new DatabaseManager();
    }

    private final MongoClient mongoClient;
    private final GuildsCollection guildsCollection;

    private DatabaseManager() {
        final MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .applyConnectionString(new ConnectionString(kBot.config.mongoConnectionString + kBot.config.mongoDatabaseName));
        final String databaseName = kBot.config.mongoDatabaseName;

        this.mongoClient = MongoClients.create(settingsBuilder.build());

        final MongoDatabase mongoDatabase = this.mongoClient.getDatabase(databaseName);
        guildsCollection = new GuildsCollection(mongoDatabase);
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void close() {
        this.mongoClient.close();
    }

    public static GuildsCollection getGuilds() {
        return DatabaseManager.instance.guildsCollection;
    }
}
