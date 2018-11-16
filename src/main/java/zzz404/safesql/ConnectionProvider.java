package zzz404.safesql;

import java.sql.Connection;

@FunctionalInterface
public interface ConnectionProvider {
    public Connection getConnection();
}
