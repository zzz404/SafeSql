package zzz404.safesql;

import zzz404.safesql.dynamic.DynamicDeleter;
import zzz404.safesql.dynamic.DynamicUpdater;
import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.dynamic.ThreeEntityQuerier;
import zzz404.safesql.dynamic.TwoEntityQuerier;
import zzz404.safesql.sql.StaticSqlExecuter;
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

    public static StaticSqlExecuter sql(String sql) {
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

    @SuppressWarnings("unchecked")
    public static <T> Field<T> field(T field) {
        QueryContext ctx = QueryContext.get();
        return (Field<T>) ctx.getLastField();
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
    public static <T> AbstractCondition cond(T fieldValue, String operator, T... values) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("cond");
        @SuppressWarnings("unchecked")
        Field<T> field = (Field<T>) ctx.takeField();
        AbstractCondition cond;
        if (ctx.hasMoreColumn()) {
            @SuppressWarnings("unchecked")
            Field<T> field2 = (Field<T>) ctx.takeField();
            cond = new MutualCondition<T>(field, operator, field2);
        }
        else {
            cond = AbstractCondition.of(field, operator, values);
        }
        ctx.addCondition(cond);
        return cond;
    }

    public static <T> void innerJoin(T fieldValue1, String operator, T fieldValue2) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("innerJoin");
        @SuppressWarnings("unchecked")
        AbstractCondition cond = new MutualCondition<T>((Field<T>) ctx.takeField(), operator,
                (Field<T>) ctx.takeField());
        ctx.addCondition(cond);
    }

    public static <T> void asc(T o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");

        @SuppressWarnings("unchecked")
        Field<T> field = (Field<T>) ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, true));
    }

    public static <T> void asc(T o, String propName) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");

        EntityGettable entityGettable = (EntityGettable) o;
        Field<T> field = new Field<>(entityGettable.entity(), propName);
        ctx.addOrderBy(new OrderBy(field, true));
    }

    public static <T> void desc(T o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");

        @SuppressWarnings("unchecked")
        Field<T> field = (Field<T>) ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, false));
    }

    public static <T> void desc(T o, String propName) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");

        EntityGettable entityGettable = (EntityGettable) o;
        Field<T> field = new Field<>(entityGettable.entity(), propName);
        ctx.addOrderBy(new OrderBy(field, false));
    }

    public static <T> T withTheSameConnection(NoisySupplier<T> supplier) {
        return use("").withTheSameConnection(supplier);
    }

    public static <T> T insert(T entity) {
        return use("").insert(entity);
    }

    public static <T> DynamicUpdater<T> update(T entity) {
        return use("").update(entity);
    }

    public static <T> DynamicDeleter<T> delete(Class<T> clazz) {
        return use("").delete(clazz);
    }
}
