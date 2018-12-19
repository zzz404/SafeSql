package zzz404.safesql;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.ThreeEntityQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.util.NoisySupplier;

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

    public static <T, U, V> ThreeEntityQuerier<T, U, V> from(Class<T> class1, Class<U> class2, Class<V> class3) {
        return use("").from(class1, class2, class3);
    }

    public static <T> Field field(T field) {
        QueryContext ctx = QueryContext.get();
        return ctx.getLastField();
    }

    public static <T> void count() {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(Field.count());
    }

    public static <T> void all(Object mockedObject) {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(Field.all((EntityGettable) mockedObject));
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
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");

        Field field = ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, true));
    }

    public static void asc(Object o, String propName) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");

        EntityGettable entityGettable = (EntityGettable) o;
        Field field = new Field(entityGettable.entity(), propName);
        ctx.addOrderBy(new OrderBy(field, true));
    }

    public static void desc(Object o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");

        Field field = ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, false));
    }

    public static void desc(Object o, String propName) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");

        EntityGettable entityGettable = (EntityGettable) o;
        Field field = new Field(entityGettable.entity(), propName);
        ctx.addOrderBy(new OrderBy(field, false));
    }

    public static <T> T withTheSameConnection(NoisySupplier<T> supplier) {
        return use("").withTheSameConnection(supplier);
    }

    public static class QuerierFactory {
        DbSourceImpl dbSource;

        public QuerierFactory(String name) {
            dbSource = DbSource.get(name);
        }

        public StaticSqlQuerier sql(String sql) {
            return new StaticSqlQuerier(dbSource).sql(sql);
        }

        public <T> OneEntityQuerier<T> from(Class<T> clazz) {
            return new OneEntityQuerier<>(dbSource, clazz);
        }

        public <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
            return new TwoEntityQuerier<>(dbSource, class1, class2);
        }

        public <T, U, V> ThreeEntityQuerier<T, U, V> from(Class<T> class1, Class<U> class2, Class<V> class3) {
            return new ThreeEntityQuerier<>(dbSource, class1, class2, class3);
        }

        public <T> T withTheSameConnection(NoisySupplier<T> supplier) {
            return DbSourceContext.withDbSource(dbSource, NoisySupplier.shutUp(supplier));
        }
    }
}
