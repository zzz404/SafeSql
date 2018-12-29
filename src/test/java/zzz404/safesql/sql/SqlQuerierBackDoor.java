package zzz404.safesql.sql;

import java.util.List;

import zzz404.safesql.sql.type.TypedValue;

public class SqlQuerierBackDoor {
    public static DbSourceImpl getDbSource(SqlQuerier q) {
        return q.dbSource;
    }

    public static String sql(SqlQuerier q) {
        return q.sql();
    }

    public static List<TypedValue<?>> paramValues(SqlQuerier q) {
        return q.paramValues();
    }

    public static int offset(SqlQuerier q) {
        return q.offset;
    }

    public static int limit(SqlQuerier q) {
        return q.limit;
    }

}
