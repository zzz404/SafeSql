package zzz404.safesql.querier;

import java.util.List;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.sql.DbSourceImpl;

public class QuerierBackDoor {
    public static DbSourceImpl getDbSource(SqlQuerier q) {
        return q.dbSource;
    }

    public static String sql(SqlQuerier q) {
        return q.sql();
    }

    public static Object[] paramValues(SqlQuerier q) {
        return q.paramValues();
    }

    public static List<AbstractCondition> conditions(DynamicQuerier q) {
        return q.conditions;
    }
}
