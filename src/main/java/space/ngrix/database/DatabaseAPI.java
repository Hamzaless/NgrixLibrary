package space.ngrix.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author amownyy
 * @since 1.0.0
 */
@Getter
public class DatabaseAPI {

    @Getter
    private static Database database;

    protected static JavaPlugin instance;

    public DatabaseAPI(JavaPlugin instance) {
        DatabaseAPI.instance = instance;
        DatabaseAPI.database = new SQLite();
    }

    public DatabaseAPI(JavaPlugin instance, String host, String port, String dbName, String userName, String password) {
        DatabaseAPI.instance = instance;
        DatabaseAPI.database = new MySQL(host, port, dbName, userName, password);
    }
}