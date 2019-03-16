package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.Page;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.sql.SqlQuerierBackDoor;

public class TestBindResultQuerier {
    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @BeforeEach
    void setUp() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_offset_and_limit() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class);
        BindResultQuerier<DocumentVo> bq = q.to(DocumentVo.class);

        bq.offset(12);
        assertEquals(12, SqlQuerierBackDoor.offset(q));

        bq.limit(7);
        assertEquals(7, SqlQuerierBackDoor.limit(q));
    }

    @Test
    void test_queryOne() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> createFakeDatabase("zzz").getMockedConnection());

        DocumentVo docVo = from(Document.class).to(DocumentVo.class).select((d, dv) -> {
            field(d.getTitle()).as(dv.getDocTitle());
        }).queryOne().get();

        assertEquals("zzz", docVo.getDocTitle());
    }

    @Test
    void test_queryList() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> createFakeDatabase("ccc", "zzz").getMockedConnection());

        List<String> titles = from(Document.class).to(DocumentVo.class).select((d, dv) -> {
            field(d.getTitle()).as(dv.getDocTitle());
        }).queryList().stream().map(DocumentVo::getDocTitle).collect(Collectors.toList());

        assertEquals(Arrays.asList("ccc", "zzz"), titles);
    }

    private FakeDatabase createFakeDatabase(String... titles) throws SQLException {
        return new FakeDatabase().addTableColumns("Document", "title")
                .addSingleColumnValues("SELECT t1.title AS docTitle FROM Document t1", "docTitle", titles);
    }

    @Test
    void test_queryPage() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> createFakeDatabase("aaa", "bbb", "ccc", "ddd", "eee")
                .addSingleColumnValues("SELECT COUNT(*) FROM Document t1", "", 5).getMockedConnection());

        Page<DocumentVo> page = from(Document.class).to(DocumentVo.class).select((d, dv) -> {
            field(d.getTitle()).as(dv.getDocTitle());
        }).offset(2).limit(2).queryPage();

        assertEquals(5, page.getTotalCount());
        List<String> titles = page.getResult().stream().map(DocumentVo::getDocTitle).collect(Collectors.toList());
        assertEquals(Arrays.asList("ccc", "ddd"), titles);
    }

    @Test
    void test_queryEntityStream() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> createFakeDatabase("ccc", "zzz").getMockedConnection());

        List<String> titles = from(Document.class).to(DocumentVo.class).select((d, dv) -> {
            field(d.getTitle()).as(dv.getDocTitle());
        }).queryEntityStream(stream -> stream.map(DocumentVo::getDocTitle).collect(Collectors.toList()));

        assertEquals(Arrays.asList("ccc", "zzz"), titles);
    }

}
