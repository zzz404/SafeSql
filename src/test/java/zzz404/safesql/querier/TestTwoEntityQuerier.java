package zzz404.safesql.querier;

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

import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.Page;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.Tuple2;

class TestTwoEntityQuerier {

    private <T, U> TwoEntityQuerier<T, U> createQuerier(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(null, class1, class2);
    }

    @Test
    void test_select() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        q.select((d, u) -> {
            d.getId();
            u.getName();
        });
        assertEquals("t1.id, t2.name", q.getColumnsClause());
    }

    @Test
    void test_where() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        q.where((d, u) -> {
            cond(d.getOwnerId(), "=", u.getId());
            cond(u.getName(), LIKE, "zz%");
        });
        assertEquals("t1.ownerId = t2.id AND t2.name LIKE ?", q.getWhereClause());
        assertEquals(Arrays.asList("zz%"), Arrays.asList(q.paramValues()));
    }

    @Test
    void test_groupBy() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        q.groupBy((d, u) -> {
            d.getId();
            u.getId();
        });
        assertEquals("t1.id, t2.id", q.getGroupByClause());
    }

    @Test
    void test_orderBy() {
        TwoEntityQuerier<Document, User> q = createQuerier(Document.class, User.class);
        q.orderBy((d, u) -> {
            asc(d.getId());
            desc(u.getId());
        });
        assertEquals("t1.id ASC, t2.id DESC", q.getOrderByClause());
    }

    @Test
    void test_queryOne() {
        Document doc = new Document();
        User user = new User();
        MyTwoEntityQuerier<Document, User> q = new MyTwoEntityQuerier<>(doc, user);

        Tuple2<Document, User> tuple = q.queryOne().get();
        assertEquals(new Tuple2<>(doc, user), tuple);
    }

    @Test
    void test_queryList() {
        Document doc = new Document();
        User user = new User();
        MyTwoEntityQuerier<Document, User> q = new MyTwoEntityQuerier<>(doc, user);

        List<Tuple2<Document, User>> list = q.queryList();
        assertEquals(Arrays.asList(new Tuple2<>(doc, user)), list);
    }

    @Test
    void test_queryPage() {
        Document doc = new Document();
        User user = new User();
        MyTwoEntityQuerier<Document, User> q = new MyTwoEntityQuerier<>(doc, user);

        Page<Tuple2<Document, User>> page = q.queryPage();
        assertEquals(new Page<>(1, Arrays.asList(new Tuple2<>(doc, user))), page);
    }

    @Test
    void test_queryEntityStream() {
        Document doc = new Document();
        User user = new User();
        MyTwoEntityQuerier<Document, User> q = new MyTwoEntityQuerier<>(doc, user);

        List<Tuple2<Document, User>> list = q.queryEntitiesStream(stream -> stream.collect(Collectors.toList()));
        assertEquals(Arrays.asList(new Tuple2<>(doc, user)), list);
    }

    @Test
    void rsToTuple() {
        TwoEntityQuerier<Document, User> q = new TwoEntityQuerier<Document, User>(null, Document.class, User.class);
        Document doc = new Document();
        User user = new User();
        q.entity1 = new MyEntity<>(1, Document.class).setT(doc);
        q.entity2 = new MyEntity<>(2, User.class).setT(user);

        Tuple2<Document, User> tuple = q.rsToTuple(null);
        assertEquals(new Tuple2<>(doc, user), tuple);
    }

    @Test
    void cover_rest() {
        createQuerier(Document.class, User.class).offset(1).limit(1);
    }

    public static class MyEntity<T> extends Entity<T> {
        private T t;

        public MyEntity(int index, Class<T> clazz) {
            super(index, clazz);
        }

        @Override
        public T mapToObject(QuietResultSet rs, Field... tableFields) {
            return t;
        }

        public MyEntity<T> setT(T t) {
            this.t = t;
            return this;
        }
    }

    public static class MyTwoEntityQuerier<T, U> extends TwoEntityQuerier<T, U> {
        private Tuple2<T, U> tuple;

        Map<String, Class<?>> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        public MyTwoEntityQuerier(T t, U u) {
            super(null, (Class<T>) t.getClass(), (Class<U>) u.getClass());
            this.tuple = new Tuple2<>(t, u);
        }

        protected Tuple2<T, U> rsToTuple(QuietResultSet rs) {
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
