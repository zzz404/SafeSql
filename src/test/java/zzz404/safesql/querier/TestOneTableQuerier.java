package zzz404.safesql.querier;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static zzz404.safesql.Sql.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import zzz404.safesql.BetweenCondition;
import zzz404.safesql.Condition;
import zzz404.safesql.InCondition;
import zzz404.safesql.OpCondition;
import zzz404.safesql.OrCondition;
import zzz404.safesql.OrderBy;
import zzz404.safesql.TableColumn;
import zzz404.safesql.helper.Document;
import zzz404.safesql.querier.OneEntityQuerier;

class TestOneTableQuerier {

    @Test
    void test_select_withAssignedFields() {
        OneEntityQuerier<Document> q = from(Document.class);
        q.select(d -> {
            d.getId();
            d.getTitle();
            d.setTitle("zzz");
        });
        assertEquals("id, title", q.getColumnsClause());
    }

    @Test
    void test_select_notCalled_meansAllFields() {
        OneEntityQuerier<Document> q = from(Document.class);

        assertEquals(1, q.tableColumns.size());
        assertEquals("*", q.getColumnsClause());
    }

    @Test
    void test_where_notCalled_meansNoCondition() {
        OneEntityQuerier<Document> q = from(Document.class);
        assertEquals(0, q.conditions.size());
    }

    @Test
    void test_where_commonOperator() {
        OneEntityQuerier<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), "=", 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new OpCondition(new TableColumn(0, "id"), "=", 3), q.conditions.get(0));
    }

    @Test
    void test_where_between() {
        OneEntityQuerier<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 1, 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new BetweenCondition(new TableColumn(0, "id"), 1, 3), q.conditions.get(0));
    }

    @Test
    void test_where_in() {
        OneEntityQuerier<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), IN, 3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
        });

        assertEquals(1, q.conditions.size());
        assertEquals(new InCondition(new TableColumn(0, "id"), 3, 1, 4, 1, 5, 9, 2, 6, 5, 3), q.conditions.get(0));
    }

    @Test
    void test_where_multiCondition() {
        OneEntityQuerier<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), ">", 3);
            cond(d.getTitle(), LIKE, "abc%");
        });
        assertEquals(2, q.conditions.size());
        assertEquals(new OpCondition(new TableColumn(0, "id"), ">", 3), q.conditions.get(0));
        assertEquals(new OpCondition(new TableColumn(0, "title"), "like", "abc%"), q.conditions.get(1));
    }

    @Test
    void test_where_or() {
        OneEntityQuerier<Document> q = from(Document.class).where(d -> {
            cond(d.getId(), "<", 2).or(d.getId(), ">", 10).or(d.getOwnerId(), "=", 1);
        });
        assertEquals(1, q.conditions.size());
        assertTrue(q.conditions.get(0) instanceof OrCondition);

        OrCondition cond = (OrCondition) q.conditions.get(0);
        List<Condition> conds = cond.subConditions;
        assertEquals(3, conds.size());

        assertEquals(new OpCondition(new TableColumn(0, "id"), "<", 2), conds.get(0));
        assertEquals(new OpCondition(new TableColumn(0, "id"), ">", 10), conds.get(1));
        assertEquals(new OpCondition(new TableColumn(0, "ownerId"), "=", 1), conds.get(2));
    }

    @Test
    void test_orderBy() {
        OneEntityQuerier<Document> q = from(Document.class).orderBy(d -> {
            asc(d.getId());
            desc(d.getTitle());
        });

        assertEquals(2, q.orderBys.size());
        assertEquals(new OrderBy("id", true), q.orderBys.get(0));
        assertEquals(new OrderBy("title", false), q.orderBys.get(1));
    }

    @Test
    void test_buildSql() {
        OneEntityQuerier<Document> q = from(Document.class).select(d -> {
            d.getId();
            d.getTitle();
        }).where(d -> {
            cond(d.getId(), ">", 12).or(d.getOwnerId(), "=", 1);
            cond(d.getTitle(), LIKE, "cc%");
        }).orderBy(d -> {
            asc(d.getId());
            desc(d.getTitle());
        });

        String sql = "SELECT id, title FROM Document"
                + " WHERE (id > ? OR ownerId = ?) AND title like ? ORDER BY id ASC, title DESC";
        assertEquals(sql, q.sql());

        sql = "SELECT COUNT(*) FROM Document" + " WHERE (id > ? OR ownerId = ?) AND title like ?";
        assertEquals(sql, q.sql_for_queryCount());
    }

    @Test
    void test_buildSql_simple() {
        OneEntityQuerier<Document> q = from(Document.class);

        String sql = "SELECT * FROM Document";
        assertEquals("SELECT * FROM Document", q.sql());

        sql = "SELECT COUNT(*) FROM Document";
        assertEquals(sql, q.sql_for_queryCount());
    }

    void coverRest() {
        from(Document.class).offset(1).limit(1);
    }
}
