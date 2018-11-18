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

    public static <T> EntityQuerier1<T> from(Class<T> clazz) {
        return use("").from(clazz);
    }

    public static <T, U> EntityQuerier2<T, U> from(Class<T> class1, Class<U> class2) {
        return use("").from(class1, class2);
    }

    @SafeVarargs
    public static <T> Condition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.get();
        Condition cond = Condition.of(ctx.takeTableColumn(), operator, values);
        ctx.conditions.add(cond);
        return cond;
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
