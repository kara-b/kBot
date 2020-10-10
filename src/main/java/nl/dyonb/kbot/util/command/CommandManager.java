package nl.dyonb.kbot.util.command;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import nl.dyonb.kbot.kBot;

import java.util.*;

public class CommandManager {

    private static CommandManager instance;

    static {
        CommandManager.instance = new CommandManager();
    }

    private final Map<String, BaseCommand> commandsMap;

    private CommandManager() {
        // Scan for Commands and initialize
        this.commandsMap = CommandManager.initialize(scanCommands().toArray(new BaseCommand[0]));
    }

    private static Map<String, BaseCommand> initialize(BaseCommand... commands) {
        final Map<String, BaseCommand> map = new LinkedHashMap<>();
        for (final BaseCommand command : commands) {
            for (final String name : command.getCommandInfo().getNames()) {
                if (map.putIfAbsent(name, command) != null) {
                    kBot.DEFAULT_LOGGER.error("Duplicate command name collision between {} and {}", name, map.get(name).getClass().getSimpleName());
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    public Map<String, BaseCommand> getCommands() {
        return this.commandsMap;
    }

    public BaseCommand getCommand(String name) {
        return this.commandsMap.get(name);
    }

    public static CommandManager getInstance() {
        return CommandManager.instance;
    }

    private static List<BaseCommand> scanCommands() {
        kBot.DEFAULT_LOGGER.info("Scanning for commands");
        List<BaseCommand> newList = new ArrayList<>();

        ScanResult scanResult = new ClassGraph().enableClassInfo().scan();
        List<String> classes = scanResult.getSubclasses(BaseCommand.class.getCanonicalName()).getNames();

        for (String classname : classes) {
            try {
                Class<?> clazz = Class.forName(classname);
                BaseCommand baseCommand = (BaseCommand) clazz.getDeclaredConstructor().newInstance();
                newList.add(baseCommand);
            } catch (Exception e) {
                kBot.DEFAULT_LOGGER.error("Couldn't scan command with the name " + classname, e);
            }
        }

        kBot.DEFAULT_LOGGER.info("Done scanning for commands, Found " + newList.size() + " commands!");
        return newList;
    }

}
