package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Page;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.SqlQuerierBackDoor;

public class TestBindResultQuerier {

    @Test
    void test_offset() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);
        bq.offset(11);
        assertEquals(11, SqlQuerierBackDoor.offset(q));
    }

    @Test
    void test_limit() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);
        bq.limit(5);
        assertEquals(5, SqlQuerierBackDoor.limit(q));
    }

    @Test
    void test_queryOne() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);

        bq.queryOne();
        assertEquals(UtilsForTest.newMap("queryOne", DocumentVo.class), q.map);
    }

    @Test
    void test_queryList() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);

        bq.queryList();
        assertEquals(UtilsForTest.newMap("queryList", DocumentVo.class), q.map);
    }

    @Test
    void test_queryPage() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);

        bq.queryPage();
        assertEquals(UtilsForTest.newMap("queryPage", DocumentVo.class), q.map);
    }

    @Test
    void test_queryEntityStream() {
        MyDynamicQuerier q = new MyDynamicQuerier();
        MyBindResultQuerier<DocumentVo> bq = new MyBindResultQuerier<>(q, DocumentVo.class);

        bq.queryEntityStream(voStream -> null);
        assertEquals(UtilsForTest.newMap("queryStream", DocumentVo.class), q.map);
    }

    public static class MyBindResultQuerier<R> extends BindResultQuerier<R> {
        public MyBindResultQuerier(DynamicQuerier querier, Class<R> resultClass) {
            super(querier, resultClass);
        }
    }

    public static class MyDynamicQuerier extends DynamicQuerier {
        Map<String, Class<?>> map = new HashMap<>();

        public MyDynamicQuerier() {
            super(null);
        }

        @Override
        public <T> Optional<T> queryOne(Class<T> clazz) {
            map.put("queryOne", clazz);
            return null;
        }

        @Override
        public <T> List<T> queryList(Class<T> clazz) {
            map.put("queryList", clazz);
            return null;
        }

        @Override
        public <T> Page<T> queryPage(Class<T> clazz) {
            map.put("queryPage", clazz);
            return null;
        }

        @Override
        public <T, R> R queryStream(Class<T> clazz, Function<Stream<T>, R> objStreamReader) {
            map.put("queryStream", clazz);
            return null;
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
