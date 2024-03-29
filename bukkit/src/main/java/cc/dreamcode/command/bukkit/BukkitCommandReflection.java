package cc.dreamcode.command.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

@UtilityClass
public class BukkitCommandReflection {

    public static SimpleCommandMap getSimpleCommandMap(Server server) {
        SimplePluginManager spm = (SimplePluginManager) server.getPluginManager();

        Field f = null;
        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        assert f != null;
        f.setAccessible(true);

        try {
            return (SimpleCommandMap) f.get(spm);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
