package zzz404.safesql.sql.proxy;

import java.sql.Connection;

import zzz404.safesql.sql.proxy.QuietConnection;

public class QuietConnectionBackDoor {
    public static Connection getConnection(QuietConnection qConn) {
        return qConn.conn;
    }
}
