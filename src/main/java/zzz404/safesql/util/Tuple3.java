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

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, t -> new Object[] { t.first, t.second, t.third });
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

}
