package nl.dyonb.kbot.util;

import discord4j.core.object.entity.Message;
import nl.dyonb.kbot.kBot;

import java.util.List;

public class kBotUtilities {

    public static List<String> splitMessage(Message message) {
        String prefix = kBot.prefix;
        String content = message.getContent().substring(prefix.length());

        List<String> list = new java.util.ArrayList<>(List.of(content.split(" ")));
        return list;
    }

    public static List<String> extractArguments(Message message) {
        List<String> list = splitMessage(message);
        list.remove(0);
        return list;
    }

}
