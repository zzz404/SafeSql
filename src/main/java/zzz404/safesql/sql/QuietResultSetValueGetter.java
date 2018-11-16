package zzz404.safesql.sql;

@FunctionalInterface
public interface QuietResultSetValueGetter<T> {
    T getValue(QuietResultSet rs, String columnName);
}
