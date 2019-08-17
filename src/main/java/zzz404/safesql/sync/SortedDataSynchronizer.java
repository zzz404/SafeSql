package zzz404.safesql.sync;

public abstract class SortedDataSynchronizer<T> {
    protected abstract SourceIterator<T> getSourceIterator();

    protected abstract DestIterator<T> getDestIterator();

    protected abstract int compareKey(T source, T desc);

    public void sync() {
        SourceIterator<T> srcIterator = getSourceIterator();
        DestIterator<T> destIterator = getDestIterator();
        T source = srcIterator.next();
        T dest = destIterator.next();
        while (source != null && dest != null) {
            int n = compareKey(source, dest);
            if (n < 0) {
                destIterator.insert(source);
                source = srcIterator.next();
            }
            else if (n > 0) {
                destIterator.remove();
                dest = destIterator.next();
            }
            else {
                destIterator.update(source);
                source = srcIterator.next();
                dest = destIterator.next();
            }
        }
        if (source == null) {
            while (dest != null) {
                destIterator.remove();
                dest = destIterator.next();
            }
        }
        else if (dest == null) {
            while (source != null) {
                destIterator.insert(source);
                source = srcIterator.next();
            }
        }
    }
}
