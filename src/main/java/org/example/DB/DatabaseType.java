package org.example.DB;

public enum DatabaseType {
    MYSQL("MySQL"),
    MONGODB("MongoDB"),
    SQLITE("SQLite");

    private final String displayName;

    DatabaseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
