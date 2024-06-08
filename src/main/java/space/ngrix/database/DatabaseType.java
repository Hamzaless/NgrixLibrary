package space.ngrix.database;

public enum DatabaseType {
    SQLite,
    MySQL;

    public static DatabaseType getDatabaseType(String type) {
        if (type.equalsIgnoreCase("SQLite"))
            return DatabaseType.SQLite;
        else
            return DatabaseType.MySQL;
    }
}