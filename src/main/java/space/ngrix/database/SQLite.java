package space.ngrix.database;

import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database API for plugin.
 *
 * @author poyrazinan
 */
public class SQLite implements Database {

    /**
     * Connects database of plugin.
     *
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String pluginName = DatabaseAPI.instance.getDescription().getName();
            File databaseFile = new File("plugins/" + pluginName + "/storage/database.db");
            databaseFile.getParentFile().mkdirs();
            String absolutePath = databaseFile.getParentFile().getAbsolutePath();
            return DriverManager.getConnection("jdbc:sqlite:" + absolutePath + "/database.db");
        }
        catch (Exception e) { e.printStackTrace(); return null; }
    }


    @Override
    public void initSQL(String query) {
        File databaseFile = new File("plugins/" + DatabaseAPI.instance.getDescription().getName() + "/database.db");
        if (!databaseFile.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:" + databaseFile.getParentFile().getAbsolutePath() + "/database.db";
                Connection connection = DriverManager.getConnection(url);
                connection.close();
            } catch(SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates table if not exists.
     * @param query Function to write query
     */
    @SneakyThrows
    @Override
    public void createTables(String query) {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        // Adding batch of table creation to the statement
        statement.addBatch(query);
        // executing batch
        statement.executeBatch();
        connection.close();
    }
}