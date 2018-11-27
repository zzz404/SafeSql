package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.BetweenCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.InCondition;
import zzz404.safesql.OpCondition;
import zzz404.safesql.OrCondition;
import zzz404.safesql.OrderBy;
import zzz404.safesql.TableField;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeConnectionFactory;

class TestOneEntityQuerier {

    @Test
    void test_select_withAssignedFields() {
        OneEntityQuerier<Document> q = createQuerier(Document.class);
        q.select(d -> {
            d.getId();
            d.getTitle();
            d.setTitle("zzz");
        });
        assertEquals("t1.id, t1.title", q.getColumnsClause());
    }

    @Test
    void test_where_notCalled_meansNoCondition() {
        OneEntityQuerier<Document> q = createQuerier(Document.class);
        assertEquals(0, q.conditions.size());
    }

    @Test
    void test_where_commonOperator() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), "=", 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new OpCondition(field("id"), "=", 3), q.conditions.get(0));
    }

    static TableField field(String name) {
        return new TableField(new Entity<Document>(1, Document.class), name);
    }

    @Test
    void test_where_between() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 1, 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new BetweenCondition(field("id"), 1, 3), q.conditions.get(0));
    }

    @Test
    void test_where_in() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), IN, 3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new InCondition(field("id"), 3, 1, 4, 1, 5, 9, 2, 6, 5, 3), q.conditions.get(0));
    }

    @Test
    void test_where_multiCondition() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), ">", 3);
            cond(d.getTitle(), LIKE, "abc%");
        });
        assertEquals(2, q.conditions.size());
        assertEquals(new OpCondition(field("id"), ">", 3), q.conditions.get(0));
        assertEquals(new OpCondition(field("title"), "like", "abc%"), q.conditions.get(1));
    }

    @Test
    void test_where_or() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), "<", 2).or(d.getId(), ">", 10).or(d.getOwnerId(), "=", 1);
        });
        assertEquals(1, q.conditions.size());
        assertTrue(q.conditions.get(0) instanceof OrCondition);

        OrCondition cond = (OrCondition) q.conditions.get(0);
        List<AbstractCondition> conds = cond.subConditions;
        assertEquals(3, conds.size());

        assertEquals(new OpCondition(field("id"), "<", 2), conds.get(0));
        assertEquals(new OpCondition(field("id"), ">", 10), conds.get(1));
        assertEquals(new OpCondition(field("ownerId"), "=", 1), conds.get(2));
    }

    @Test
    void test_orderBy() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).orderBy(d -> {
            asc(d.getId());
            desc(d.getTitle());
        });

        assertEquals(2, q.orderBys.size());
        assertEquals(new OrderBy("t1.id", true), q.orderBys.get(0));
        assertEquals(new OrderBy("t1.title", false), q.orderBys.get(1));
    }

    @Test
    void test_buildSql() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).select(d -> {
            d.getId();
            d.getTitle();
        }).where(d -> {
            cond(d.getId(), ">", 12).or(d.getOwnerId(), "=", 1);
            cond(d.getTitle(), LIKE, "cc%");
        }).orderBy(d -> {
            asc(d.getId());
            desc(d.getTitle());
        });

        String sql = "SELECT t1.id, t1.title FROM Document t1"
                + " WHERE (t1.id > ? OR t1.ownerId = ?) AND t1.title like ? ORDER BY t1.id ASC, t1.title DESC";
        assertEquals(sql, q.sql());

        sql = "SELECT COUNT(*) FROM Document t1 WHERE (t1.id > ? OR t1.ownerId = ?) AND t1.title like ?";
        assertEquals(sql, q.sql_for_queryCount());
    }

    @Test
    void test_buildSql_simple() {
        OneEntityQuerier<Document> q = createQuerier(Document.class);
        assertEquals("SELECT * FROM Document t1", q.sql());
        assertEquals("SELECT COUNT(*) FROM Document t1", q.sql_for_queryCount());
    }

    private <T> OneEntityQuerier<T> createQuerier(Class<T> clazz) {
        return new OneEntityQuerier<>(new FakeConnectionFactory(null), clazz);
    }

    void coverRest() {
        createQuerier(Document.class).offset(1).limit(1);
    }
}
