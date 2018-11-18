package zzz404.safesql;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "in";
    public static final String LIKE = "like";

    private static QuerierFactory querierFactory = new QuerierFactory();

    private Sql() {
    }

    public static QuerierFactory use() {
        return use("");
    }

    public static QuerierFactory use(String name) {
        QueryContext.create(name);
        return querierFactory;
    }

    public static StaticSqlQuerier sql(String sql) {
        return use("").sql(sql);
    }

    public static <T> OneTableQuerier<T> from(Class<T> clazz) {
        return use("").from(clazz);
    }

    public static <T, U> TwoTableQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return use("").from(class1, class2);
    }

    @SafeVarargs
    public static <T> Condition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.get();
        TableColumn tableColumn = ctx.takeTableColumn();
        Condition cond;
        if (ctx.hasMoreColumn()) {
            cond = new MutualCondition(tableColumn, operator, ctx.takeTableColumn());
        }
        else {
            cond = Condition.of(tableColumn, operator, values);
        }
        ctx.conditions.add(cond);
        return cond;
    }

    public static <T> void innerJoin(T field1, String operator, T field2) {
        QueryContext ctx = QueryContext.get();
        Condition cond = new MutualCondition(ctx.takeTableColumn(), operator, ctx.takeTableColumn());
        ctx.conditions.add(cond);
    }

    public static void asc(Object o) {
        addOrderByToContext(true);
    }

    private static void addOrderByToContext(boolean isAsc) {
        QueryContext ctx = QueryContext.get();
        ctx.orderBys.add(new OrderBy(ctx.takeTableColumn(), isAsc));
    }

    public static void desc(Object o) {
        addOrderByToContext(false);
    }
}
