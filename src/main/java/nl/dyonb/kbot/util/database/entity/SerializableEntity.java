package nl.dyonb.kbot.util.database.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.dyonb.kbot.util.database.DatabaseManager;
import nl.dyonb.kbot.util.database.bean.Bean;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;

public abstract class SerializableEntity<T extends Bean> {

    private final T bean;

    public SerializableEntity(T bean) {
        this.bean = bean;
    }

    public T getBean() {
        return this.bean;
    }

    public Document toDocument() {
        try {
            return Document.parse(DatabaseManager.objectMapper.writeValueAsString(this.getBean()),
                    new DocumentCodec(DatabaseManager.codecRegistry));
        } catch (final JsonProcessingException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public String toString() {
        return "SerializableEntity{" +
                "bean=" + this.bean +
                '}';
    }
}
