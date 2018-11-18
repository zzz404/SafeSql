package zzz404.safesql.util;

@FunctionalInterface
public interface MainValueExtractor<T> {
    Object[] extract(T t);
}