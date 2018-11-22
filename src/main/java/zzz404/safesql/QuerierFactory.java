package zzz404.safesql;

public class QuerierFactory {
    String name;

    public QuerierFactory(String name) {
        this.name = name;
    }

    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(name).sql(sql);
    }

    public <T> OneTableQuerier<T> from(Class<T> clazz) {
        return new OneTableQuerier<>(name, clazz);
    }

    public <T, U> TwoTableQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoTableQuerier<>(name, class1, class2);
    }
}