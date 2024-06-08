package space.ngrix;

import org.bukkit.plugin.java.JavaPlugin;

import space.ngrix.config.*;
import space.ngrix.utils.*;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class NgrixAPI {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static String prefix;


    @Getter
    private static boolean initialized = false;

    public NgrixAPI(JavaPlugin plugin, String pluginName) {
        instance = plugin;
        prefix = pluginName;
        initialized = true;
    }

    public static Logger getLogger() {
        return instance.getLogger();
    }

    public static ConfigAPI getConfigAPI() {
        return new ConfigAPI(instance);
    }

    public static NgrixAuth getNgrixAuth(String licenseKey, String pluginName) {
        return new NgrixAuth(licenseKey, pluginName);
    }

    public static PluginUpdater getPluginUpdater(String pluginName, String version) {
        return new PluginUpdater(pluginName, version);
    }


    public static ConfigAPI getConfigAPI(JavaPlugin instance) {
        return new ConfigAPI(instance);
    }


}
