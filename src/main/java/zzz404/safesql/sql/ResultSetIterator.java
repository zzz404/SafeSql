package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.Utils;

public class ResultSetIterator implements Iterator<ResultSet> {
    private ResultSet rs;
    private int limit = -1;

    private boolean hasNext = true;
    private int count_of_dataInput = 0;
    private int count_of_dataOutput = 0;

    public ResultSetIterator(ResultSet rs) {
        this(rs, null, null);
    }

    public ResultSetIterator(ResultSet rs, Integer start, Integer limit) {
        this.rs = rs;
        if (start != null) {
            try {
                this.rs.absolute(start);
            }
            catch (SQLException e) {
                throw Utils.throwRuntime(e);
            }
        }
        if (limit != null) {
            this.limit = limit;
        }
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
                Validate.isTrue(count_of_dataInput - count_of_dataOutput == 1);
                return true;
            }
            Validate.isTrue(count_of_dataInput == count_of_dataOutput);
            try {
                hasNext = rs.next();
            }
            catch (SQLException e) {
                throw Utils.throwRuntime(e);
            }
            if (hasNext) {
                count_of_dataInput++;
            }
            return hasNext;
        }
    }

    @Override
    public ResultSet next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        if (count_of_dataInput > count_of_dataOutput) {
            Validate.isTrue(count_of_dataInput - count_of_dataOutput == 1);
            count_of_dataOutput++;
            return rs;
        }
        else {
            Validate.isTrue(count_of_dataInput == count_of_dataOutput);
            if (limit >= 0 && count_of_dataOutput >= limit) {
                throw new NoSuchElementException();
            }
            try {
                hasNext = rs.next();
            }
            catch (SQLException e) {
                throw Utils.throwRuntime(e);
            }
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