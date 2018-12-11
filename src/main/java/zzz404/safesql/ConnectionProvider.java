package zzz404.safesql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionProvider {
    public Connection getConnection() throws SQLException;
}
