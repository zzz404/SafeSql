package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "in";
    public static final String LIKE = "like";

    private Sql() {
    }

    public static QuerierFactory use() {
        return use("");
    }

    public static QuerierFactory use(String name) {
        return new QuerierFactory(name);
    }

    public static StaticSqlQuerier sql(String sql) {
        return use("").sql(sql);
    }

    public static <T> OneEntityQuerier<T> from(Class<T> clazz) {
        return use("").from(clazz);
    }

    public static <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return use("").from(class1, class2);
    }

    @SafeVarargs
    public static <T> Condition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("cond");
        TableColumn tableColumn = ctx.takeTableColumn();
        Condition cond;
        if (ctx.hasMoreColumn()) {
            cond = new MutualCondition(tableColumn, operator, ctx.takeTableColumn());
        }
        else {
            cond = Condition.of(tableColumn, operator, values);
        }
        ctx.addCondition(cond);
        return cond;
    }

    public static <T> void innerJoin(T field1, String operator, T field2) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("innerJoin");
        Condition cond = new MutualCondition(ctx.takeTableColumn(), operator, ctx.takeTableColumn());
        ctx.addCondition(cond);
    }

    public static void asc(Object o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");
        String columnName;
        if(o instanceof String && !ctx.hasMoreColumn()) {
            columnName = (String) o;
        }
        else {
            columnName = ctx.takeTableColumn().getPrefixedColumnName();
        }
        ctx.addOrderBy(columnName, true);
    }

    public static void desc(Object o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");
        ctx.addOrderBy(ctx.takeTableColumn().getPrefixedColumnName(), false);
    }

}
