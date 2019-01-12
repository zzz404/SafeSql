package zzz404.safesql;

import zzz404.safesql.dynamic.AbstractCondition;
import zzz404.safesql.dynamic.DynamicDeleter;
import zzz404.safesql.dynamic.DynamicInserter;
import zzz404.safesql.dynamic.DynamicUpdater;
import zzz404.safesql.dynamic.EntityGettable;
import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.MutualCondition;
import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.dynamic.OrderBy;
import zzz404.safesql.dynamic.QueryContext;
import zzz404.safesql.dynamic.ThreeEntityQuerier;
import zzz404.safesql.dynamic.TwoEntityQuerier;
import zzz404.safesql.sql.StaticSqlExecuter;
import zzz404.safesql.util.NoisySupplier;

public class SafeSql {

    public static final String BETWEEN = "between";
    public static final String IN = "IN";
    public static final String LIKE = "LIKE";

    private SafeSql() {
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

    public static Field field(Object field) {
        QueryContext ctx = QueryContext.get();
        return ctx.getLastField();
    }

    public static <T> void count() {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(FieldImpl.count());
    }

    public static <T> void all(Object mockedObject) {
        QueryContext ctx = QueryContext.get();
        ctx.addTableField(FieldImpl.all((EntityGettable) mockedObject));
    }

    @SafeVarargs
    public static AbstractCondition cond(Object fieldValue, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("cond");
        FieldImpl field = ctx.takeField();
        AbstractCondition cond;
        if (ctx.hasMoreColumn()) {
            FieldImpl field2 = ctx.takeField();
            cond = new MutualCondition(field, operator, field2);
        }
        else {
            cond = AbstractCondition.of(field, operator, values);
        }
        ctx.addCondition(cond);
        return cond;
    }

    public static <T> void asc(T o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("asc");

        FieldImpl field = ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, true));
    }

    public static <T> void desc(T o) {
        QueryContext ctx = QueryContext.get();
        ctx.getScope().checkCommand("desc");

        FieldImpl field = ctx.takeField();
        ctx.addOrderBy(new OrderBy(field, false));
    }

    public static <T> T withTheSameConnection(NoisySupplier<T> supplier) {
        return use("").withTheSameConnection(supplier);
    }

    public static <T> DynamicInserter<T> insert(T entity) {
        return use("").insert(entity);
    }

    public static <T> DynamicUpdater<T> update(T entity) {
        return use("").update(entity);
    }

    public static <T> DynamicDeleter<T> delete(Class<T> clazz) {
        return use("").delete(clazz);
    }

}
