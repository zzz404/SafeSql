package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.User;
import zzz404.safesql.reflection.ThreeObjectPlayer;

public class TestThreeEntityBindResultQuerier {

    @Test
    void test_select() {
        ThreeEntityQuerier<Document, User, Category> q = new ThreeEntityQuerier<>(null, Document.class, User.class,
                Category.class);
        ThreeEntityBindResultQuerier<Document, User, Category, DocumentVo> bq = q.to(DocumentVo.class);
        bq.select((d, u, c, v) -> {
            field(d.getTitle()).as(v.getTitle2());
            u.getId();
            c.getId();
        });
        assertEquals("t1.title, t2.id, t3.id", q.getColumnsClause());
    }

    @Test
    void test_where() {
        @SuppressWarnings("unchecked")
        ThreeEntityQuerier<Document, User, Category> q = mock(ThreeEntityQuerier.class);
        ThreeEntityBindResultQuerier<Document, User, Category, DocumentVo> bq = new ThreeEntityBindResultQuerier<>(q,
                DocumentVo.class);
        ThreeObjectPlayer<Document, User, Category> conditionsCollector = (d, u, c) -> {
        };
        bq.where(conditionsCollector);
        verify(q, times(1)).where(conditionsCollector);
    }

    @Test
    void test_groupBy() {
        @SuppressWarnings("unchecked")
        ThreeEntityQuerier<Document, User, Category> q = mock(ThreeEntityQuerier.class);
        ThreeEntityBindResultQuerier<Document, User, Category, DocumentVo> bq = new ThreeEntityBindResultQuerier<>(q,
                DocumentVo.class);
        ThreeObjectPlayer<Document, User, Category> conditionsCollector = (d, u, c) -> {
        };
        bq.groupBy(conditionsCollector);
        verify(q, times(1)).groupBy(conditionsCollector);
    }

    @Test
    void test_orderBy() {
        @SuppressWarnings("unchecked")
        ThreeEntityQuerier<Document, User, Category> q = mock(ThreeEntityQuerier.class);
        ThreeEntityBindResultQuerier<Document, User, Category, DocumentVo> bq = new ThreeEntityBindResultQuerier<>(q,
                DocumentVo.class);
        ThreeObjectPlayer<Document, User, Category> conditionsCollector = (d, u, c) -> {
        };
        bq.orderBy(conditionsCollector);
        verify(q, times(1)).orderBy(conditionsCollector);
    }

}
