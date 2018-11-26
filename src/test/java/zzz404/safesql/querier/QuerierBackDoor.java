package zzz404.safesql.querier;

import zzz404.safesql.sql.ConnectionFactoryImpl;

public class QuerierBackDoor {
    public static ConnectionFactoryImpl getConnFactory(SqlQuerier q) {
        return q.connFactory;
    }
}
