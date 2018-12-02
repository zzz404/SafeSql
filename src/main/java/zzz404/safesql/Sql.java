package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "IN";
    public static final String LIKE = "LIKE";

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

    public static <T> Field field(T field) {
        QueryContext ctx = QueryContext.get();
        return ctx.getLastField();
    }

    public static <T> void count() {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(Field.count());
    }

    public static <T> void count(T field) {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(Field.count(ctx.takeField()));
    }

    public static <T> void all(T mockedObject) {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(Field.all(mockedObject));
    }

    @SafeVarargs
    public static <T> AbstractCondition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("cond");
        Field tableColumn = ctx.takeField();
        AbstractCondition cond;
        if (ctx.hasMoreColumn()) {
            cond = new MutualCondition(tableColumn, operator, ctx.takeField());
        }
        else {
            cond = AbstractCondition.of(tableColumn, operator, values);
        }
        ctx.addCondition(cond);
        return cond;
    }

    public static <T> void innerJoin(T field1, String operator, T field2) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("innerJoin");
        AbstractCondition cond = new MutualCondition(ctx.takeField(), operator, ctx.takeField());
        ctx.addCondition(cond);
    }

    public static void asc(Object o) {
        QueryContext.get().getScope().checkCommand("asc");
        addOrderBy(o, true);
    }

    private static void addOrderBy(Object o, boolean isAsc) {
        QueryContext ctx = QueryContext.get();
        String columnName;
        if (o instanceof String && !ctx.hasMoreColumn()) {
            columnName = (String) o;
            ctx.addOrderBy(new OrderBy(columnName, isAsc));
        }
        else {
            ctx.addOrderBy(new OrderBy(ctx.takeField(), isAsc));
        }
    }

    public static void desc(Object o) {
        QueryContext.get().getScope().checkCommand("desc");
        addOrderBy(o, false);
    }

}
