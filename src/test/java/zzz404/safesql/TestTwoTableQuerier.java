package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;

class TestTwoTableQuerier {

    @AfterEach
    void afterEach() {
        QueryContext.clear();
    }

    @Test
    void test_select_withAssignedFields() {
        TwoTableQuerier<Document, User> q = from(Document.class, User.class);
        q.select((d, u) -> {
            d.getId();
            d.getTitle();
            d.setTitle("zzz");
            u.getName();
        });
        assertEquals("t1.id, t1.title, t2.name", q.getColumnsClause());
    }

    @Test
    void test_select_notCalled_meansAllFields() {
        TwoTableQuerier<Document, User> q = from(Document.class, User.class);

        assertEquals(1, q.tableColumns.size());
        assertEquals("*", q.getColumnsClause());
    }

    @Test
    void test_where_notCalled_meansNoCondition() {
        TwoTableQuerier<Document, User> q = from(Document.class, User.class);
        assertEquals(0, q.conditions.size());
    }

    // @Test
    // void test_where_commonOperator() {
    // OneTableQuerier<Document> q = from(Document.class).where(d -> {
    // cond(d.getId(), "=", 3);
    // });
    // }

    // assertEquals(1, q.conditions.size());
    // assertEquals(new OpCondition(new TableColumn(0, "id"), "=", 3),
    // q.conditions.get(0));
    // }
    //
    // @Test
    // void test_where_between() {
    // OneTableQuerier<Document> q = from(Document.class).where(d -> {
    // cond(d.getId(), BETWEEN, 1, 3);
    // });
    //
    // assertEquals(1, q.conditions.size());
    // assertEquals(new BetweenCondition(new TableColumn(0, "id"), 1, 3),
    // q.conditions.get(0));
    // }
    //
    // @Test
    // void test_where_in() {
    // OneTableQuerier<Document> q = from(Document.class).where(d -> {
    // cond(d.getId(), IN, 3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
    // });
    //
    // assertEquals(1, q.conditions.size());
    // assertEquals(new InCondition(new TableColumn(0, "id"), 3, 1, 4, 1, 5, 9,
    // 2, 6, 5, 3), q.conditions.get(0));
    // }
    //
    // @Test
    // void test_where_multiCondition() {
    // OneTableQuerier<Document> q = from(Document.class).where(d -> {
    // cond(d.getId(), ">", 3);
    // cond(d.getTitle(), LIKE, "abc%");
    // });
    // assertEquals(2, q.conditions.size());
    // assertEquals(new OpCondition(new TableColumn(0, "id"), ">", 3),
    // q.conditions.get(0));
    // assertEquals(new OpCondition(new TableColumn(0, "title"), "like",
    // "abc%"), q.conditions.get(1));
    // }
    //
    // @Test
    // void test_where_or() {
    // OneTableQuerier<Document> q = from(Document.class).where(d -> {
    // cond(d.getId(), "<", 2).or(d.getId(), ">", 10).or(d.getOwnerId(), "=",
    // 1);
    // });
    // assertEquals(1, q.conditions.size());
    // assertTrue(q.conditions.get(0) instanceof OrCondition);
    //
    // OrCondition cond = (OrCondition) q.conditions.get(0);
    // List<Condition> conds = cond.subConditions;
    // assertEquals(3, conds.size());
    //
    // assertEquals(new OpCondition(new TableColumn(0, "id"), "<", 2),
    // conds.get(0));
    // assertEquals(new OpCondition(new TableColumn(0, "id"), ">", 10),
    // conds.get(1));
    // assertEquals(new OpCondition(new TableColumn(0, "ownerId"), "=", 1),
    // conds.get(2));
    // }
    //
    // @Test
    // void test_orderBy() {
    // OneTableQuerier<Document> q = from(Document.class).orderBy(d -> {
    // asc(d.getId());
    // desc(d.getTitle());
    // });
    //
    // assertEquals(2, q.orderBys.size());
    // assertEquals(new OrderBy(new TableColumn(0, "id"), true),
    // q.orderBys.get(0));
    // assertEquals(new OrderBy(new TableColumn(0, "title"), false),
    // q.orderBys.get(1));
    // }
    //
    // @Test
    // void test_buildSql() {
    // OneTableQuerier<Document> q = from(Document.class).select(d -> {
    // d.getId();
    // d.getTitle();
    // }).where(d -> {
    // cond(d.getId(), ">", 12).or(d.getOwnerId(), "=", 1);
    // cond(d.getTitle(), LIKE, "cc%");
    // }).orderBy(d -> {
    // asc(d.getId());
    // desc(d.getTitle());
    // });
    //
    // String sql = "SELECT id, title FROM Document"
    // + " WHERE (id > ? OR ownerId = ?) AND title like ? ORDER BY id ASC, title
    // DESC";
    // assertEquals(sql, q.buildSql());
    //
    // sql = "SELECT COUNT(*) FROM Document" + " WHERE (id > ? OR ownerId = ?)
    // AND title like ?";
    // assertEquals(sql, q.buildSql_for_queryCount());
    // }
    //
    // @Test
    // void test_buildSql_simple() {
    // OneTableQuerier<Document> q = from(Document.class);
    //
    // String sql = "SELECT * FROM Document";
    // assertEquals("SELECT * FROM Document", q.buildSql());
    //
    // sql = "SELECT COUNT(*) FROM Document";
    // assertEquals(sql, q.buildSql_for_queryCount());
    // }
    //
    // void coverRest() {
    // from(Document.class).offset(1).limit(1);
    // }

}