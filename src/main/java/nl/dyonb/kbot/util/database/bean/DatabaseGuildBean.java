package nl.dyonb.kbot.util.database.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import reactor.util.annotation.Nullable;

public class DatabaseGuildBean implements Bean {

    @JsonProperty("_id")
    private String id;

    @Nullable
    @JsonProperty("prefix")
    private String prefix;

    public DatabaseGuildBean(String id, @Nullable String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    public DatabaseGuildBean(String id) {
        this(id, null);
    }

    // No args constructor for use in serialization
    public DatabaseGuildBean() { }

    public String getId() {
        return id;
    }

    @Nullable
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(@Nullable String prefix) {
        this.prefix = prefix;
    }
}
