package zzz404.safesql;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static zzz404.safesql.Sql.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Test;

class TestSqlQuery1 {

    @Test
    void test_select_withAssignedFields() {
        SqlQuerier1<Document> q = from(Document.class);
        q.select(d -> {
            d.getId();
            d.getTitle();
            d.setTitle("zzz");
        });
        List<String> fields = Arrays.asList("id", "title");
        assertTrue(ListUtils.isEqualList(fields, q.fields));
    }

    @Test
    void test_select_notCalled_meansAllFields() {
        SqlQuerier1<Document> q = from(Document.class).build();

        assertEquals(1, q.fields.size());
        assertEquals("*", q.fields.get(0));
    }

    @Test
    void test_where_notCalled_meansNoCondition() {
        SqlQuerier1<Document> q = from(Document.class);
        q.build();
        assertEquals(0, q.conditions.size());
    }

    @Test
    void test_where_commonOperator() {
        SqlQuerier1<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), "=", 3);
        }).build();

        assertEquals(1, q.conditions.size());
        assertEquals(new OpCondition("id", "=", 3), q.conditions.get(0));
    }

    @Test
    void test_where_between() {
        SqlQuerier1<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 1, 3);
        }).build();

        assertEquals(1, q.conditions.size());
        assertEquals(new BetweenCondition("id", 1, 3), q.conditions.get(0));
    }

    @Test
    void test_where_in() {
        SqlQuerier1<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), IN, 3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
        }).build();

        assertEquals(1, q.conditions.size());
        assertEquals(new InCondition("id", 3, 1, 4, 1, 5, 9, 2, 6, 5, 3),
                q.conditions.get(0));
    }

    @Test
    void test_where_multiCondition() {
        SqlQuerier1<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), ">", 3);
            cond(d.getTitle(), LIKE, "abc%");
        }).build();
        assertEquals(2, q.conditions.size());
        assertEquals(new OpCondition("id", ">", 3), q.conditions.get(0));
        assertEquals(new OpCondition("title", "like", "abc%"),
                q.conditions.get(1));
    }

    @Test
    void test_where_or() {
        SqlQuerier1<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), "<", 2).or(d.getId(), ">", 10);
        }).build();
        assertEquals(1, q.conditions.size());
        assertTrue(q.conditions.get(0) instanceof OrCondition);

        OrCondition cond = (OrCondition) q.conditions.get(0);
        List<Condition> conds = cond.subConditions;
        assertEquals(2, conds.size());

        assertEquals(new OpCondition("id", "<", 2), conds.get(0));
        assertEquals(new OpCondition("id", ">", 10), conds.get(1));
    }
}
