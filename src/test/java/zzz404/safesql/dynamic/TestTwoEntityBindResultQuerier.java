package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.SafeSql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.User;
import zzz404.safesql.reflection.TwoObjectPlayer;

public class TestTwoEntityBindResultQuerier {

    @Test
    void test_select() {
        TwoEntityQuerier<Document, User> q = new TwoEntityQuerier<>(null, Document.class, User.class);
        TwoEntityBindResultQuerier<Document, User, DocumentVo> bq = q.to(DocumentVo.class);
        bq.select((d, u, v) -> {
            field(d.getTitle()).as(v.getTitle2());
            u.getId();
        });
        assertEquals("t1.title, t2.id", q.getColumnsClause());
    }

    @Test
    void test_where() {
        @SuppressWarnings("unchecked")
        TwoEntityQuerier<Document, User> q = mock(TwoEntityQuerier.class);
        TwoEntityBindResultQuerier<Document, User, DocumentVo> bq = new TwoEntityBindResultQuerier<>(q,
                DocumentVo.class);
        TwoObjectPlayer<Document, User> conditionsCollector = (d, u) -> {
        };
        bq.where(conditionsCollector);
        verify(q, times(1)).where(conditionsCollector);
    }

    @Test
    void test_groupBy() {
        @SuppressWarnings("unchecked")
        TwoEntityQuerier<Document, User> q = mock(TwoEntityQuerier.class);
        TwoEntityBindResultQuerier<Document, User, DocumentVo> bq = new TwoEntityBindResultQuerier<>(q,
                DocumentVo.class);
        TwoObjectPlayer<Document, User> conditionsCollector = (d, u) -> {
        };
        bq.groupBy(conditionsCollector);
        verify(q, times(1)).groupBy(conditionsCollector);
    }

    @Test
    void test_orderBy() {
        @SuppressWarnings("unchecked")
        TwoEntityQuerier<Document, User> q = mock(TwoEntityQuerier.class);
        TwoEntityBindResultQuerier<Document, User, DocumentVo> bq = new TwoEntityBindResultQuerier<>(q,
                DocumentVo.class);
        TwoObjectPlayer<Document, User> conditionsCollector = (d, u) -> {
        };
        bq.orderBy(conditionsCollector);
        verify(q, times(1)).orderBy(conditionsCollector);
    }

}
