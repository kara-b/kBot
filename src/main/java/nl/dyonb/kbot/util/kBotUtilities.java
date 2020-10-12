package nl.dyonb.kbot.util;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import nl.dyonb.kbot.kBot;

import java.util.List;
import java.util.Random;

public class kBotUtilities {

    public static List<String> splitMessage(Message message, String prefix) {
        String content = message.getContent().substring(prefix.length());

        return new java.util.ArrayList<>(List.of(content.split(" ")));
    }

    public static List<String> extractArguments(Message message, String prefix) {
        List<String> list = splitMessage(message, prefix);
        list.remove(0);
        return list;
    }

    public static Color randomBrightColor() {
        Random random = new Random();
        float r = random.nextFloat() / 2f + 0.5f;
        float g = random.nextFloat() / 2f + 0.5f;
        float b = random.nextFloat() / 2f + 0.5f;
        return Color.of(r, g, b);
    }

}
