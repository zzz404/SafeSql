package zzz404.safesql;

public class QuerierFactory {
    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier().sql(sql);
    }

    public <T> OneTableQuerier<T> from(Class<T> clazz) {
        return new OneTableQuerier<>(clazz);
    }

    public <T, U> TwoTableQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoTableQuerier<>(class1, class2);
    }
}