package zzz404.safesql.util;

public class Tuple2<T, U> {
    private T first;
    private U second;

    public Tuple2(T t, U u) {
        this.first = t;
        this.second = u;
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, t -> new Object[] { t.first, t.second });
    }
}
