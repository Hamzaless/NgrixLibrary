package space.ngrix.database;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL implements Database {

    private String host, port, dbName, userName, password;

    /**
     * Constructor of MySQL
     * @param host
     * @param port
     * @param dbName
     * @param userName
     * @param password
     */
    public MySQL(String host, String port, String dbName, String userName, String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Connection of SQL
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initialize the sql
     */
    @Override
    public void initSQL(String query) {
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