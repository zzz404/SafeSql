package zzz404.safesql.sql;

import java.sql.Connection;

public class QuietConnectionBackDoor {
    public static Connection getConnection(QuietConnection qConn) {
        return qConn.conn;
    }
}
