package zzz404.safesql.sync;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestSortedDataSynchronizer {

    @Test
    public void test_sync() {
        MySourceIterator srcIterator = new MySourceIterator(1, 3, 5, 7);
        MyDestIterator descIterator = new MyDestIterator(3, 4, 5, 6, 8);
        MySortedDataSynchronizer syncer = new MySortedDataSynchronizer(srcIterator,
                descIterator);
        syncer.sync();
        assertEquals(Arrays.asList(1, 7), descIterator.inserted);
        assertEquals(Arrays.asList(3, 5), descIterator.updated);
        assertEquals(Arrays.asList(4, 6, 8), descIterator.deleted);
    }

    public static class MySortedDataSynchronizer extends SortedDataSynchronizer<Integer> {
        private SourceIterator<Integer> srcIterator;
        private DestIterator<Integer> descIterator;

        public MySortedDataSynchronizer(SourceIterator<Integer> srcIterator, DestIterator<Integer> descIterator) {
            this.srcIterator = srcIterator;
            this.descIterator = descIterator;
        }

        @Override
        protected SourceIterator<Integer> getSourceIterator() {
            return srcIterator;
        }

        @Override
        protected DestIterator<Integer> getDestIterator() {
            return descIterator;
        }

        @Override
        protected int compareKey(Integer source, Integer desc) {
            return source.compareTo(desc);
        }
    }

    public static class MySourceIterator implements SourceIterator<Integer> {
        protected Integer[] ns;
        protected int index = -1;

        public MySourceIterator(Integer... ns) {
            this.ns = ns;
        }

        @Override
        public Integer next() {
            index++;
            if (index >= ns.length) {
                return null;
            }
            return ns[index];
        }
    }

    public static class MyDestIterator extends MySourceIterator implements DestIterator<Integer> {
        List<Integer> inserted = new LinkedList<>();
        List<Integer> updated = new LinkedList<>();
        List<Integer> deleted = new LinkedList<>();

        public MyDestIterator(Integer... ns) {
            super(ns);
        }

        @Override
        public void insert(Integer data) {
            inserted.add(data);
        }

        @Override
        public void update(Integer data) {
            updated.add(data);
        }

        @Override
        public void remove() {
            deleted.add(ns[index]);
        }

    }
}
