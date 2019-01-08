package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.OrderBy;

public class CoverOrderBy {

    @Test
    void coverOrderBy() {
        assertEquals("zzz", new MyOrderBy().toString());
    }

    static class MyOrderBy extends OrderBy {

        public MyOrderBy() {
            super(null, false);
        }

        @Override
        public String toClause() {
            return "zzz";
        }

    }
}
