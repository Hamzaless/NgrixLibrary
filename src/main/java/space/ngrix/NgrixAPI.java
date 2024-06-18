package space.ngrix;

import org.bukkit.plugin.java.JavaPlugin;

import space.ngrix.config.*;
import space.ngrix.utils.*;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * This class provides the main API for the Ngrix plugin.
 * It provides methods to get instances of various utility classes.
 */
public class NgrixAPI {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static String prefix;

    @Getter
    private static boolean initialized = false;

    /**
     * Constructor for the NgrixAPI class.
     * @param plugin The JavaPlugin instance.
     * @param pluginName The name of the plugin.
     */
    public NgrixAPI(JavaPlugin plugin, String pluginName) {
        instance = plugin;
        prefix = pluginName;
        initialized = true;
    }

    /**
     * Returns the logger for the plugin.
     * @return The logger for the plugin.
     */
    public static Logger getLogger() {
        return instance.getLogger();
    }

    /**
     * Returns a new instance of the ConfigAPI class.
     * @return A new instance of the ConfigAPI class.
     */
    public static ConfigAPI getConfigAPI() {
        return new ConfigAPI(instance);
    }

    /**
     * Returns a new instance of the NgrixAuth class.
     * @param licenseKey The license key to be checked.
     * @param pluginName The name of the plugin to be checked.
     * @return A new instance of the NgrixAuth class.
     */
    public static NgrixAuth getNgrixAuth(String licenseKey, String pluginName) {
        return new NgrixAuth(licenseKey, pluginName);
    }

    /**
     * Returns a new instance of the PluginUpdater class.
     * @param pluginName The name of the plugin to be checked.
     * @param version The version of the plugin to be checked.
     * @return A new instance of the PluginUpdater class.
     */
    public static PluginUpdater getPluginUpdater(String pluginName, String version) {
        return new PluginUpdater(pluginName, version);
    }

    /**
     * Returns a new instance of the ConfigAPI class.
     * @param instance The JavaPlugin instance.
     * @return A new instance of the ConfigAPI class.
     */
    public static ConfigAPI getConfigAPI(JavaPlugin instance) {
        return new ConfigAPI(instance);
    }
}