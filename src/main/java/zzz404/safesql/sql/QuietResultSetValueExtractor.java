package zzz404.safesql.sql;

@FunctionalInterface
public interface QuietResultSetValueExtractor<T> {
    T extract(QuietResultSet rs, int index);

    default T extractFirst(QuietResultSet rs) {
        return extract(rs, 1);
    }
}
