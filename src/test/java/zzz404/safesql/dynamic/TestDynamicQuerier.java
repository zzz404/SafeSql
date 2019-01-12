package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.Page;
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeSchemaBase;
import zzz404.safesql.helper.User;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.SqlQuerierBackDoor;

class TestDynamicQuerier {

    @Test
    void test_scopeOrderError_throwException() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onSelectScope(() -> {
        });
        q.onGroupByScope(() -> {
        });
        assertThrows(IllegalArgumentException.class, () -> q.onWhereScope(() -> {
        }));
    }

    @Test
    void test_getColumnsClause_noFields_selectAll() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        assertEquals("*", q.getColumnsClause());
    }

    @Test
    void test_getColumnsClause_hasFields() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onSelectScope(() -> {
            QueryContext ctx = QueryContext.get();
            ctx.addTableField(new FieldImpl<>(new Entity<>(1, Document.class), "title"));
            ctx.addTableField(new FieldImpl<>(new Entity<>(2, User.class), "account"));
        });
        assertEquals("t1.title, t2.account", q.getColumnsClause());
    }

    @Test
    void test_getConditionsClause_and_paramValues() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onWhereScope(() -> {
            QueryContext ctx = QueryContext.get();
            ctx.addCondition(
                    AbstractCondition.of(new FieldImpl<>(new Entity<>(1, Document.class), "title"), "=", "zzz"));
            ctx.addCondition(AbstractCondition.of(new FieldImpl<>(new Entity<>(2, User.class), "id"), "=", 123));
        });
        assertEquals("t1.title = ? AND t2.id = ?", q.getWhereClause());
        assertEquals(UtilsForTest.createTypedValueList("zzz", 123), q.paramValues());
    }

    @Test
    void test_getGroupByClause() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onGroupByScope(() -> {
            QueryContext ctx = QueryContext.get();
            ctx.addTableField(new FieldImpl<>(new Entity<>(1, Document.class), "title"));
            ctx.addTableField(new FieldImpl<>(new Entity<>(2, User.class), "account"));
        });
        assertEquals("t1.title, t2.account", q.getGroupByClause());
    }

    @Test
    void test_getOrderByClause() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onOrderByScope(() -> {
            QueryContext ctx = QueryContext.get();
            ctx.addOrderBy(new OrderBy(new FieldImpl<>(new Entity<>(1, Document.class), "title"), true));
            ctx.addOrderBy(new OrderBy(new FieldImpl<>(new Entity<>(2, User.class), "account"), false));
        });
        assertEquals("t1.title ASC, t2.account DESC", q.getOrderByClause());
    }

    @Test
    void test_getTablesClause_noFields_returnAllTables() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.entities.add(new Entity<>(1, Object.class));
        q.entities.add(new Entity<>(2, Document.class));
        q.entities.add(new Entity<>(3, User.class));

        assertEquals("Object t1, Document t2, User t3", q.getTablesClause());
    }

    @Test
    void test_getTablesClause() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Document> entity1 = new Entity<>(1, Document.class);
        q.entities.add(entity1);
        Entity<User> entity2 = new Entity<>(2, User.class);
        q.entities.add(entity2);

        assertEquals("Document t1, User t2", q.getTablesClause());
    }

    @Test
    void test_sql_and__sql_for_queryCount__simple() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.entities.add(new Entity<>(1, Document.class));
        assertEquals("SELECT * FROM Document t1", q.sql());
        assertEquals("SELECT COUNT(*) FROM Document t1", q.sql_for_queryCount());
    }

    @Test
    void test_sql_and__sql_for_queryCount() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Document> docEntity = new Entity<>(1, Document.class);
        Entity<User> userEntity = new Entity<>(2, User.class);
        q.entities.add(docEntity);
        q.entities.add(userEntity);
        q.fields = Arrays.asList(new FieldImpl<>(docEntity, "title"), new FieldImpl<>(userEntity, "account"));
        q.conditions = Arrays.asList(
                new MutualCondition<>(new FieldImpl<>(docEntity, "ownerId"), "=", new FieldImpl<>(userEntity, "id")));
        q.groupBys = Arrays.asList(new FieldImpl<>(userEntity, "account"));
        q.orderBys = Arrays.asList(new OrderBy(new FieldImpl<>(docEntity, "id"), true));
        String expectedSql = "SELECT t1.title, t2.account FROM Document t1, User t2"
                + " WHERE t1.ownerId = t2.id GROUP BY t2.account ORDER BY t1.id ASC";
        assertEquals(expectedSql, q.sql());
        expectedSql = "SELECT COUNT(*) FROM Document t1, User t2" + " WHERE t1.ownerId = t2.id GROUP BY t2.account";
        assertEquals(expectedSql, q.sql_for_queryCount());
    }

    @Test
    void test_count() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());
        OneEntityQuerier<Document> querier = from(Document.class).select(d -> {
            count();
            d.getId();
        });
        assertEquals("SELECT COUNT(*), t1.id FROM Document t1", SqlQuerierBackDoor.sql(querier));
    }

    @Test
    void test_all() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());
        TwoEntityQuerier<Document, User> querier = from(Document.class, User.class).select((d, u) -> {
            all(d);
            u.getId();
        });
        assertEquals("SELECT t1.*, t2.id FROM Document t1, User t2", SqlQuerierBackDoor.sql(querier));
    }

    @Test
    void test_asc_desc() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());

        ThreeEntityQuerier<Document, User, Category> querier = from(Document.class, User.class, Category.class)
                .orderBy((d, u, c) -> {
                    asc(d.getId());
                    desc(c.getId());
                    asc(u.getName());
                });
        assertEquals("SELECT * FROM Document t1, User t2, Category t3 ORDER BY t1.id ASC, t3.id DESC, t2.name ASC",
                SqlQuerierBackDoor.sql(querier));
    }

    @Test
    void test_innerJoin_() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());

        TwoEntityQuerier<Document, User> querier = from(Document.class, User.class).where((d, u) -> {
            innerJoin(d.getOwnerId(), "=", u.getId());
        });
        String sql = SqlQuerierBackDoor.sql(querier);
        assertEquals("SELECT * FROM Document t1, User t2 WHERE t1.ownerId = t2.id", sql);
    }

    public static class MyDynamicQuerier extends DynamicQuerier {
        public MyDynamicQuerier() {
            super(null);
            this.dbSource = new DbSourceImpl("");
            this.dbSource.snakeFormCompatable(false);
        }

        @Override
        public Object queryOne() {
            return null;
        }

        @Override
        public List<?> queryList() {
            return null;
        }

        @Override
        public Page<?> queryPage() {
            return null;
        }

    }
}
