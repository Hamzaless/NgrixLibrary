package space.ngrix.config;

import space.ngrix.config.backend.Config;
import space.ngrix.config.backend.Json;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class ConfigAPI {

    protected static JavaPlugin instance;

    public ConfigAPI(JavaPlugin plugin) {
        instance = plugin;
    }

    /**
     * Initiating config file
     *
     * @param fileName name of the file
     * @return Config
     * @see Config
     */
    public Config initConfig(String fileName) {
        return initConfig(fileName, instance);
    }

    /**
     * Initiating lang file into lang folder
     *
     * @param fileName name of the file
     * @return Config
     * @see Config
     */
    public Config initLangFile(String fileName) {
        Config config = initConfig("lang/" + fileName, instance);
        if (config == null)
            config = initConfig("lang/en", instance);
        return config;
    }



    /**
     * Initiating config file for modules
     *
     * @param moduleName name of the module
     * @return Config
     * @see Config
     */
    public Config initModuleConfig(String moduleName, String fileName, Plugin plugin, InputStream inputStream) {
        fileName += ".yml";
        return new Config(fileName, "plugins/" + plugin.getDescription().getName() + "/modules/" + moduleName,
                inputStream);
    }

    /**
     * Initiating config file for addons
     *
     * @param fileName name of the file
     * @param plugin  plugin instance
     * @return Config
     * @see Config
     */
    public Config initConfig(String fileName, @NotNull JavaPlugin plugin) {
        fileName += ".yml";
        return new Config(fileName, "plugins/" + plugin.getDescription().getName(),
                instance.getResource(fileName));
    }

    /**
     * Initiating json file
     *
     * @param fileName name of the file
     * @return Json
     * @see Json
     */
    public Json initJson(String fileName) {
        return initJson(fileName, instance);
    }

    /**
     * Initiating json file
     *
     * @param fileName name of the file
     * @param plugin main class of project
     * @return Json
     * @see Json
     */
    public Json initJson(String fileName, @NotNull JavaPlugin plugin) {
        fileName += ".json";
        return new Json(fileName, "plugins/" + plugin.getDescription().getName(),
                instance.getResource(fileName));
    }
}