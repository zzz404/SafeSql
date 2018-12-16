package zzz404.safesql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.FakeSchemaBase;
import zzz404.safesql.helper.Record;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetMetaData;

class TestEntity {
    @Test
    void test_mapToObject_cacheOrMapper() {
        Entity<Document> entity = new Entity<>(1, Document.class);
        assertNull(entity.orMapper);

        QuietResultSet rs = createSimpleResultSet();

        entity.mapToObject(rs);
        OrMapper<Document> orMapper;
        assertNotNull(orMapper = entity.orMapper);

        entity.mapToObject(rs);
        OrMapper<Document> orMapper2 = entity.orMapper;
        assertEquals(orMapper, orMapper2);

        entity.mapToObject(createSimpleResultSet());
        orMapper2 = entity.orMapper;
        assertNotEquals(orMapper, orMapper2);
    }

    private QuietResultSet createSimpleResultSet() {
        QuietResultSet rs = mock(QuietResultSet.class);
        QuietResultSetMetaData meta = mock(QuietResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(0);
        return rs;
    }

    @Test
    void test_mapToObject_mapAssignedFields() throws SQLException {
        ResultSet rs = new FakeDatabase().pushRecords(new Record().setValue("title", "zzz").setValue("ownerId", 123))
                .getNextResultSet();
        ResultSetMetaData metaData = FakeSchemaBase.mockMetaData("title", "ownerId");
        when(rs.getMetaData()).thenReturn(metaData);

        Entity<Document> entity = new Entity<>(1, Document.class);
        QuietResultSet qrs = new QuietResultSet(rs);
        qrs.next();
        Document doc = entity.mapToObject(qrs, new Field(entity, "title"));
        assertEquals("zzz", doc.getTitle());
        assertNull(doc.getOwnerId());
    }

}
