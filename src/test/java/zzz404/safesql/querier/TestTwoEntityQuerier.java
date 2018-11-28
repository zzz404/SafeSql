package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeConnectionFactory;
import zzz404.safesql.helper.User;

class TestTwoEntityQuerier {

    @Test
    void test_select_withAssignedFields() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        q.select((d, u) -> {
            d.getId();
            d.getTitle();
            d.setTitle("zzz");
            u.getName();
        });
        assertEquals("t1.id, t1.title, t2.name", q.getColumnsClause());
    }

    private <T, U> TwoEntityQuerier<T, U> createQuerier(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(new FakeConnectionFactory(null), class1, class2);
    }

    @Test
    void test_where_notCalled_meansNoCondition() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        assertEquals(0, q.conditions.size());
    }

}
