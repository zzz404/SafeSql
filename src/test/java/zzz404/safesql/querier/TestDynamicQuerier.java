package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Condition;
import zzz404.safesql.Page;
import zzz404.safesql.QueryContext;
import zzz404.safesql.TableField;

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
        assertEquals(1, q.tableFields.size());
        assertEquals(field, q.tableFields.get(0));
    }

    @Test
    void test_onWhereScope() {
        AbstractCondition cond = mock(AbstractCondition.class);
        MyDynamicQuerier q = new MyDynamicQuerier();
        q.onWhereScope(() -> {
            QueryContext.get().addCondition(cond);
        });
        assertEquals(1, q.conditions.size());
        Condition cond1 = q.conditions.get(0);
        assertEquals(cond, cond1);
    }

    public static class MyDynamicQuerier extends DynamicQuerier {
        public MyDynamicQuerier() {
            super(null);
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
