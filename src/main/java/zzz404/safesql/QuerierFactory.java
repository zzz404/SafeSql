package zzz404.safesql;

public class QuerierFactory {
    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(sql);
    }

    public <T> EntityQuerier1<T> from(Class<T> clazz) {
        return new EntityQuerier1<>(clazz);
    }

    public <T, U> EntityQuerier2<T, U> from(Class<T> class1, Class<U> class2) {
        return new EntityQuerier2<>(class1, class2);
    }
}