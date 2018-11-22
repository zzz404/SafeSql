package zzz404.safesql.util;

public class Tuple3<T, U, V> {
    private T first;
    private U second;
    private V third;

    public Tuple3(T t, U u, V v) {
        this.first = t;
        this.second = u;
        this.third = v;
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    public V third() {
        return third;
    }
}
