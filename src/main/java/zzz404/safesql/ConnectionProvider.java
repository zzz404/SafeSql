package zzz404.safesql;

import java.sql.Connection;

public abstract class ConnectionProvider {
    static ConnectionProvider instance;

    public abstract Connection getConnection();
    
    public static void register(ConnectionProvider provider) {
        ConnectionProvider.instance = provider;
    }
}
