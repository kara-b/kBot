package nl.dyonb.kbot.util.database.collection;

import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;

public class DatabaseCollection {
    private final MongoCollection<Document> collection;

    protected DatabaseCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public MongoCollection<Document> getCollection() {
        return this.collection;
    }
}
