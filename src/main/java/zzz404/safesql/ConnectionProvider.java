package zzz404.safesql;

import java.sql.Connection;

import zzz404.safesql.sql.QuietConnection;

public abstract class ConnectionProvider {
    static ConnectionProvider instance;

    public abstract Connection getConnection();

    public final QuietConnection getQuietConnection() {
        return new QuietConnection(getConnection());
    }

    public static void register(ConnectionProvider provider) {
        ConnectionProvider.instance = provider;
    }
}
