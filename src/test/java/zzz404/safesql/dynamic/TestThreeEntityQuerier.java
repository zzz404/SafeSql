package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Page;
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.Tuple3;

class TestThreeEntityQuerier {

    private <T, U, V> ThreeEntityQuerier<T, U, V> createQuerier(Class<T> class1, Class<U> class2, Class<V> class3) {
        return new ThreeEntityQuerier<>(null, class1, class2, class3);
    }

    @Test
    void test_select() {
        ThreeEntityQuerier<Document, User, Category> q = createQuerier(Document.class, User.class, Category.class);
        q.select((d, u, c) -> {
            d.getId();
            u.getName();
            c.getId();
        });
        assertEquals("t1.id, t2.name, t3.id", q.getColumnsClause());
    }

    @Test
    void test_where() {
        ThreeEntityQuerier<Document, User, Category> q = createQuerier(Document.class, User.class, Category.class);
        q.where((d, u, c) -> {
            cond(d.getOwnerId(), "=", u.getId());
            cond(u.getName(), LIKE, "zz%");
            cond(c.getId(), "=", 321);
        });
        assertEquals("t1.ownerId = t2.id AND t2.name LIKE ? AND t3.id = ?", q.getWhereClause());
        assertEquals(Arrays.asList("zz%", 321), Arrays.asList(q.paramValues()));
    }

    @Test
    void test_groupBy() {
        ThreeEntityQuerier<Document, User, Category> q = createQuerier(Document.class, User.class, Category.class);
        q.groupBy((d, u, c) -> {
            d.getId();
            u.getId();
            c.getName();
        });
        assertEquals("t1.id, t2.id, t3.name", q.getGroupByClause());
    }

    @Test
    void test_orderBy() {
        ThreeEntityQuerier<Document, User, Category> q = createQuerier(Document.class, User.class, Category.class);
        q.orderBy((d, u, c) -> {
            asc(d.getId());
            desc(u.getId());
            asc(c.getId());
        });
        assertEquals("t1.id ASC, t2.id DESC, t3.id ASC", q.getOrderByClause());
    }

    @Test
    void test_queryOne() {
        Document doc = new Document();
        User user = new User();
        Category cat = new Category();
        MyThreeEntityQuerier<Document, User, Category> q = new MyThreeEntityQuerier<>(doc, user, cat);

        Tuple3<Document, User, Category> tuple = q.queryOne().get();
        assertEquals(new Tuple3<>(doc, user, cat), tuple);
    }

    @Test
    void test_queryList() {
        Document doc = new Document();
        User user = new User();
        Category cat = new Category();
        MyThreeEntityQuerier<Document, User, Category> q = new MyThreeEntityQuerier<>(doc, user, cat);

        List<Tuple3<Document, User, Category>> list = q.queryList();
        assertEquals(Arrays.asList(new Tuple3<>(doc, user, cat)), list);
    }

    @Test
    void test_queryPage() {
        Document doc = new Document();
        User user = new User();
        Category cat = new Category();
        MyThreeEntityQuerier<Document, User, Category> q = new MyThreeEntityQuerier<>(doc, user, cat);

        Page<Tuple3<Document, User, Category>> page = q.queryPage();
        assertEquals(new Page<>(1, Arrays.asList(new Tuple3<>(doc, user, cat))), page);
    }

    @Test
    void test_queryEntityStream() {
        Document doc = new Document();
        User user = new User();
        Category cat = new Category();
        MyThreeEntityQuerier<Document, User, Category> q = new MyThreeEntityQuerier<>(doc, user, cat);

        List<Tuple3<Document, User, Category>> list = q
                .queryEntitiesStream(stream -> stream.collect(Collectors.toList()));
        assertEquals(Arrays.asList(new Tuple3<>(doc, user, cat)), list);
    }

    @Test
    void cover_rest() {
        createQuerier(Document.class, User.class, Category.class).offset(1).limit(1);
    }

    public static class MyThreeEntityQuerier<T, U, V> extends ThreeEntityQuerier<T, U, V> {
        private Tuple3<T, U, V> tuple;

        Map<String, Class<?>> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        public MyThreeEntityQuerier(T t, U u, V v) {
            super(null, (Class<T>) t.getClass(), (Class<U>) u.getClass(), (Class<V>) v.getClass());
            this.tuple = new Tuple3<>(t, u, v);
        }

        protected Tuple3<T, U, V> rsToTuple(QuietResultSet rs) {
            return tuple;
        }

        @Override
        public <E> Optional<E> queryOne(Function<QuietResultSet, E> mapper) {
            return Optional.of(mapper.apply(null));
        }

        @Override
        public <E> List<E> queryList_by_mapEach(Function<QuietResultSet, E> mapper) {
            return (List<E>) Arrays.asList(mapper.apply(null));
        }

        @Override
        public <E> Page<E> queryPage_by_mapEach(Function<QuietResultSet, E> mapper) {
            List<E> result = Arrays.asList(mapper.apply(null));
            return new Page<>(1, result);
        }

        @Override
        public <E> E queryStream(Function<Stream<QuietResultSet>, E> rsStreamReader) {
            Stream<QuietResultSet> rsStream = Stream.of((QuietResultSet) null);
            return rsStreamReader.apply(rsStream);
        }

    }
}
