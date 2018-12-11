package zzz404.safesql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public interface ConnectionManager {
    <T> T underConnection(Function<Connection, T> func) throws SQLException;
}
