package zzz404.safesql.sql;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QuietResultSetIterator implements Iterator<QuietResultSet> {
    private QuietResultSet rs;
    private int limit = 0;

    private boolean hasNext = true;
    private int count_of_dataInput = 0;
    private int count_of_dataOutput = 0;

    public QuietResultSetIterator(QuietResultSet rs, int offset, int limit) {
        this.rs = rs;
        if (offset > 0) {
            this.rs.absolute(offset);
        }
        this.limit = limit;
        this.hasNext();
    }

    @Override
    public boolean hasNext() {
        if (!hasNext) {
            return false;
        }
        else {
            if (limit >= 0 && this.count_of_dataOutput >= limit) {
                return false;
            }
            if (count_of_dataInput > count_of_dataOutput) {
                return true;
            }
            hasNext = rs.next();
            if (hasNext) {
                count_of_dataInput++;
            }
            return hasNext;
        }
    }

    @Override
    public QuietResultSet next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        if (count_of_dataInput > count_of_dataOutput) {
            count_of_dataOutput++;
            return rs;
        }
        else {
            if (limit >= 0 && count_of_dataOutput >= limit) {
                throw new NoSuchElementException();
            }
            hasNext = rs.next();
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            else {
                count_of_dataInput++;
                count_of_dataOutput++;
                return rs;
            }
        }
    }
}