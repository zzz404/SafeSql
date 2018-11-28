package zzz404.safesql.querier;

import zzz404.safesql.sql.DbSourceImpl;

public class QuerierBackDoor {
    public static DbSourceImpl getDbSource(SqlQuerier q) {
        return q.dbSource;
    }
}
