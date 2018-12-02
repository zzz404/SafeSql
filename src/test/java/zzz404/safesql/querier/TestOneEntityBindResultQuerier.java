package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.reflection.OneObjectPlayer;

public class TestOneEntityBindResultQuerier {

    @Test
    void test_select() {
        OneEntityQuerier<Document> q = new OneEntityQuerier<>(null, Document.class);
        OneEntityBindResultQuerier<Document, DocumentVo> bq = q.to(DocumentVo.class);
        bq.select((d, v) -> {
            field(d.getTitle()).as(v.getTitle2());
        });
        assertEquals("t1.title", q.getColumnsClause());
        assertEquals(UtilsForTest.newMap("title", "title2"), bq.columnMap);
    }

    @Test
    void test_where() {
        @SuppressWarnings("unchecked")
        OneEntityQuerier<Document> q = mock(OneEntityQuerier.class);
        OneEntityBindResultQuerier<Document, DocumentVo> bq = new OneEntityBindResultQuerier<>(q, DocumentVo.class);
        OneObjectPlayer<Document> conditionsCollector = d -> {
        };
        bq.where(conditionsCollector);
        verify(q, times(1)).where(conditionsCollector);
    }

    @Test
    void test_groupBy() {
        @SuppressWarnings("unchecked")
        OneEntityQuerier<Document> q = mock(OneEntityQuerier.class);
        OneEntityBindResultQuerier<Document, DocumentVo> bq = new OneEntityBindResultQuerier<>(q, DocumentVo.class);
        OneObjectPlayer<Document> conditionsCollector = d -> {
        };
        bq.groupBy(conditionsCollector);
        verify(q, times(1)).groupBy(conditionsCollector);
    }

    @Test
    void test_orderBy() {
        @SuppressWarnings("unchecked")
        OneEntityQuerier<Document> q = mock(OneEntityQuerier.class);
        OneEntityBindResultQuerier<Document, DocumentVo> bq = new OneEntityBindResultQuerier<>(q, DocumentVo.class);
        OneObjectPlayer<Document> conditionsCollector = d -> {
        };
        bq.orderBy(conditionsCollector);
        verify(q, times(1)).orderBy(conditionsCollector);
    }

}
