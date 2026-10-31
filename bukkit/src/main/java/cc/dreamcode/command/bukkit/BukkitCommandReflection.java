package cc.dreamcode.command.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@UtilityClass
public class BukkitCommandReflection {

    public static SimpleCommandMap getSimpleCommandMap(Server server) {
        try {
            Method getCommandMapMethod = server.getClass().getMethod("getCommandMap");
            Object map = getCommandMapMethod.invoke(server);
            if (map instanceof SimpleCommandMap) {
                return (SimpleCommandMap) map;
            }
        }
        catch (Exception ignored) {
        }

        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            Object map = commandMapField.get(server);
            if (map instanceof SimpleCommandMap) {
                return (SimpleCommandMap) map;
            }
        }
        catch (Exception ignored) {
        }

        try {
            PluginManager pm = server.getPluginManager();
            Field f = pm.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            Object map = f.get(pm);
            if (map instanceof SimpleCommandMap) {
                return (SimpleCommandMap) map;
            }
        }
        catch (Exception ignored) {
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Command> getKnownCommands(SimpleCommandMap commandMap) {
        try {
            Method getKnownCommandsMethod = commandMap.getClass().getMethod("getKnownCommands");
            return (Map<String, Command>) getKnownCommandsMethod.invoke(commandMap);
        }
        catch (Exception ignored) {
        }

        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            return (Map<String, Command>) field.get(commandMap);
        }
        catch (Exception ignored) {
            return null;
        }
    }
}
