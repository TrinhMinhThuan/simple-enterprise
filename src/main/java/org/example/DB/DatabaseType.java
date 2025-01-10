package org.example.DB;

public enum DatabaseType {
    MYSQL("MySQL"), MONGODB("MongoDB");

    private final String displayName;

    DatabaseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
