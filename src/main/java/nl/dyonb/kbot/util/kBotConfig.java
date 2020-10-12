package nl.dyonb.kbot.util;

import io.github.protonmc.tiny_config.ConfigManager;
import io.github.protonmc.tiny_config.Configurable;
import io.github.protonmc.tiny_config.Saveable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class kBotConfig implements Saveable {
    public ConfigManager configManager;

    public kBotConfig(String location) {
        try {
            Path path = Paths.get(location);

            configManager = new ConfigManager(path);
            configManager.load();
            configManager.loadObject(this);
            configManager.save(Collections.singleton(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSerializedId() {
        return "kbot";
    }

    @Configurable public String token = "";
    @Configurable public String defaultPrefix = "k!";

    @Configurable public String bot_source = "https://github.com/kara-b/kBot";
    @Configurable public String supportServer = "https://discord.gg/qzGj4En";

    @Configurable public String mongoConnectionString = "mongodb://127.0.0.1:27017/";
    @Configurable public String mongoDatabaseName = "";
    @Configurable public String mongoGuildsName = "guilds";
}
