package zzz404.safesql.sync;

public interface DestIterator<T> {
    T next();

    void insert(T sourceData);

    void update(T sourceData);

    void remove();
}
