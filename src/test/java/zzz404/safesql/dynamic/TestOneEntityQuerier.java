package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Page;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.UtilsForTest;

class TestOneEntityQuerier {

    @Test
    void test_to() {
        OneEntityQuerier<Document> q = createQuerier(Document.class);
        OneEntityBindResultQuerier<Document, DocumentVo> q2 = q.to(DocumentVo.class);
        assertEquals(q, q2.querier);
        assertEquals(DocumentVo.class, q2.resultEntity.getObjClass());
    }

    @Test
    void test_select() {
        OneEntityQuerier<Document> q = createQuerier(Document.class);
        q.select(d -> {
            d.getId();
        });
        assertEquals("t1.id", q.getColumnsClause());
    }

    @Test
    void test_where() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).where(d -> {
            cond(d.getId(), "=", 3);
        });
        assertEquals(Arrays.asList(new OpCondition<>(new FieldImpl<>(new Entity<>(1, Document.class), "id"), "=", 3)),
                q.conditions);
    }

    @Test
    void test_groupBy() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).groupBy(d -> {
            d.getId();
        });
        assertEquals(Arrays.asList(new FieldImpl<>(new Entity<>(1, Document.class), "id")), q.groupBys);
    }

    @Test
    void test_orderBy() {
        OneEntityQuerier<Document> q = createQuerier(Document.class).orderBy(d -> {
            asc(d.getId());
        });
        assertEquals(Arrays.asList(new OrderBy(new FieldImpl<>(new Entity<>(1, Document.class), "id"), true)), q.orderBys);
    }

    private <T> OneEntityQuerier<T> createQuerier(Class<T> clazz) {
        return new OneEntityQuerier<>(null, clazz);
    }

    @Test
    void test_queryOne() {
        MyOneEntityQuerier<Document> q = new MyOneEntityQuerier<>(Document.class);
        q.queryOne();
        Map<String, Class<?>> map = UtilsForTest.newMap("queryOne", Document.class);
        assertEquals(map, q.map);
    }

    @Test
    void test_queryList() {
        MyOneEntityQuerier<Document> q = new MyOneEntityQuerier<>(Document.class);
        q.queryList();
        Map<String, Class<?>> map = UtilsForTest.newMap("queryList", Document.class);
        assertEquals(map, q.map);
    }

    @Test
    void test_queryPage() {
        MyOneEntityQuerier<Document> q = new MyOneEntityQuerier<>(Document.class);
        q.queryPage();
        Map<String, Class<?>> map = UtilsForTest.newMap("queryPage", Document.class);
        assertEquals(map, q.map);
    }

    @Test
    void test_queryEntityStream() {
        MyOneEntityQuerier<Document> q = new MyOneEntityQuerier<>(Document.class);
        q.queryEntityStream(stream -> null);
        Map<String, Class<?>> map = UtilsForTest.newMap("queryStream", Document.class);
        assertEquals(map, q.map);
    }

    @Test
    void cover_rest() {
        createQuerier(Document.class).offset(1).limit(1);
    }

    public static class MyOneEntityQuerier<T> extends OneEntityQuerier<T> {

        Map<String, Class<?>> map = new HashMap<>();

        public MyOneEntityQuerier(Class<T> clazz) {
            super(null, clazz);
        }

        @Override
        public <E> Optional<E> queryOne(Class<E> clazz) {
            map.put("queryOne", clazz);
            return null;
        }

        @Override
        public <E> List<E> queryList(Class<E> clazz) {
            map.put("queryList", clazz);
            return null;
        }

        @Override
        public <R> Page<R> queryPage(Class<R> clazz) {
            map.put("queryPage", clazz);
            return null;
        }

        @Override
        public <E, R> R queryStream(Class<E> clazz, Function<Stream<E>, R> objStreamReader) {
            map.put("queryStream", clazz);
            return null;
        }

    }
}
