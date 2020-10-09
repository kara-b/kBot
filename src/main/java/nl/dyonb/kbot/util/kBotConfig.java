package nl.dyonb.kbot.util;

import io.github.protonmc.tiny_config.ConfigManager;
import io.github.protonmc.tiny_config.Configurable;
import io.github.protonmc.tiny_config.Saveable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class kBotConfig implements Saveable {
    public ConfigManager configManager;

    public kBotConfig(String location) {
        try {
            Path path = Paths.get(location);

            configManager = new ConfigManager(path);
            configManager.load();
            configManager.loadObject(this);
            configManager.save(Arrays.asList(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSerializedId() {
        return "kbot";
    }

    @Configurable
    public String token = "";
}
