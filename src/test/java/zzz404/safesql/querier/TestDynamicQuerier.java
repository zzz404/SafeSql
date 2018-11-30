package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.OrderBy;
import zzz404.safesql.Page;
import zzz404.safesql.QueryContext;
import zzz404.safesql.TableField;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;
import zzz404.safesql.sql.DbSourceImpl;

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
    void test_onSelectScope() {
        TableField field = mock(TableField.class);
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onSelectScope(() -> {
            QueryContext.get().addTableField(field);
        });
        assertEquals(Arrays.asList(field), q.tableFields);
    }

    @Test
    void test_onWhereScope() {
        AbstractCondition cond = mock(AbstractCondition.class);
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onWhereScope(() -> {
            QueryContext.get().addCondition(cond);
        });
        assertEquals(Arrays.asList(cond), q.conditions);
    }

    @Test
    void test_onGroupByScope() {
        TableField field = mock(TableField.class);
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onGroupByScope(() -> {
            QueryContext.get().addTableField(field);
        });
        assertEquals(Arrays.asList(field), q.groupBys);
    }

    @Test
    void test_onOrderByScope() {
        OrderBy orderBy = mock(OrderBy.class);
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onOrderByScope(() -> {
            QueryContext.get().addOrderBy(orderBy);
        });
        assertEquals(Arrays.asList(orderBy), q.orderBys);
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
    void test_getTablesClause_hasFields_returnUsedTables() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Object> entity1 = new Entity<>(1, Object.class);
        q.entities.add(entity1);
        Entity<Document> entity2 = new Entity<>(2, Document.class);
        q.entities.add(entity2);
        Entity<User> entity3 = new Entity<>(3, User.class);
        q.entities.add(entity3);

        q.tableFields = Arrays.asList(new TableField(entity2, ""), new TableField(entity3, ""));

        assertEquals("Document t2, User t3", q.getTablesClause());
    }

    @Test
    void test_getTablesClause_hasFields_returnUsedTables_countCondition() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Object> entity1 = new Entity<>(1, Object.class);
        q.entities.add(entity1);
        Entity<Document> entity2 = new Entity<>(2, Document.class);
        q.entities.add(entity2);
        Entity<User> entity3 = new Entity<>(3, User.class);
        q.entities.add(entity3);

        q.tableFields = Arrays.asList(new TableField(entity1, ""));
        q.conditions = Arrays.asList(AbstractCondition.of(new TableField(entity2, ""), "=", 1));

        assertEquals("Object t1, Document t2", q.getTablesClause());
    }

    @Test
    void test_getTablesClause_hasFields_returnUsedTables_countGroupBy() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Object> entity1 = new Entity<>(1, Object.class);
        q.entities.add(entity1);
        Entity<Document> entity2 = new Entity<>(2, Document.class);
        q.entities.add(entity2);
        Entity<User> entity3 = new Entity<>(3, User.class);
        q.entities.add(entity3);

        q.tableFields = Arrays.asList(new TableField(entity1, ""));
        q.groupBys = Arrays.asList(new TableField(entity3, ""));

        assertEquals("Object t1, User t3", q.getTablesClause());
    }

    @Test
    void test_getTablesClause_hasFields_returnUsedTables_countOrderBy() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        Entity<Object> entity1 = new Entity<>(1, Object.class);
        q.entities.add(entity1);
        Entity<Document> entity2 = new Entity<>(2, Document.class);
        q.entities.add(entity2);
        Entity<User> entity3 = new Entity<>(3, User.class);
        q.entities.add(entity3);

        q.tableFields = Arrays.asList(new TableField(entity2, ""));
        q.orderBys = Arrays.asList(new OrderBy(new TableField(entity3, ""), true));

        assertEquals("Document t2, User t3", q.getTablesClause());
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
