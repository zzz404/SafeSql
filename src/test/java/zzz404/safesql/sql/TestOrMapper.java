package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.DocumentVo;
import zzz404.safesql.helper.JdbcMocker;
import zzz404.safesql.helper.Record;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.util.CommonUtils;

public class TestOrMapper {

    @Test
    void test_mapToObject() throws SQLException {
        Record record = new Record().setValue("ownerId", 23).setValue("title", "ttt").setValue("tege", "regegr");
        ResultSet rs = JdbcMocker.mockResultSet(record);
        rs.next();

        OrMapper<Document> mapper = new OrMapper<>(Document.class, new QuietResultSet(rs));
        Document doc = mapper.mapToObject();

        assertNull(doc.getId());
        assertEquals(new Integer(23), doc.getOwnerId());
        assertEquals("ttt", doc.getTitle());
    }

    @Test
    void test_mapToObject_limitedColumns() throws SQLException {
        Record record = new Record().setValue("ownerId", 23).setValue("title", "ttt").setValue("tege", "regegr");
        ResultSet rs = JdbcMocker.mockResultSet(record);
        rs.next();

        OrMapper<Document> mapper = new OrMapper<>(Document.class, new QuietResultSet(rs))
                .selectColumns(CommonUtils.newSet("title", "dfyh"));
        Document doc = mapper.mapToObject();

        assertNull(doc.getId());
        assertNull(doc.getOwnerId());
        assertEquals("ttt", doc.getTitle());
    }

    @Test
    void test_mapToObject_mapColumn() throws SQLException {
        Record record = new Record().setValue("ownerId", 23).setValue("title", "ttt").setValue("tege", "regegr")
                .setValue("category", "zzz");
        ResultSet rs = JdbcMocker.mockResultSet(record);
        rs.next();

        OrMapper<DocumentVo> mapper = new OrMapper<>(DocumentVo.class, new QuietResultSet(rs));
        DocumentVo doc = mapper.mapToObject(UtilsForTest.newMap("title", "title2"));

        assertEquals(new Integer(23), doc.getOwnerId());
        assertEquals("ttt", doc.getTitle2());
        assertNull(doc.getCategory());
    }

    @Test
    void getColumnsOfResultSet() throws SQLException {
        Record record = new Record().setValue("title", "ttt");
        ResultSet rs = JdbcMocker.mockResultSet(record);
        rs.next();

        OrMapper<DocumentVo> mapper = new OrMapper<>(DocumentVo.class, new QuietResultSet(rs));

        Set<String> columns = mapper.getColumnsOfResultSet();
        Set<String> columns2 = mapper.getColumnsOfResultSet();
        assertTrue(columns == columns2);
    }

}
