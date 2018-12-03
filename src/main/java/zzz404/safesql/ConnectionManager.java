package zzz404.safesql;

import java.sql.Connection;
import java.util.function.Function;

public interface ConnectionManager {
    <T> T underConnection(Function<Connection, T> func);
}
