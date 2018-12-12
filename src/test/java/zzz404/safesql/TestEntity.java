package zzz404.safesql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.sql.OrMapper;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetMetaData;

class TestEntity {
    @Test
    void test_mapToObject_cacheOrMapper() {
        Entity<Document> entity = new Entity<>(1, Document.class);
        assertNull(entity.orMapper);

        QuietResultSet rs = createSimpleResultSet();
        
        entity.mapToObject(rs, Collections.emptyList());
        OrMapper<Document> orMapper;
        assertNotNull(orMapper = entity.orMapper);

        entity.mapToObject(rs, Collections.emptyList());
        OrMapper<Document> orMapper2 = entity.orMapper;
        assertEquals(orMapper, orMapper2);

        entity.mapToObject(createSimpleResultSet(), Collections.emptyList());
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
    void test_mapToObject() {
        Entity<Document> entity = new Entity<>(1, Document.class);
        assertNull(entity.orMapper);

        QuietResultSet rs = createSimpleResultSet();
        
        entity.mapToObject(rs, Collections.emptyList());
        OrMapper<Document> orMapper;
        assertNotNull(orMapper = entity.orMapper);

        entity.mapToObject(rs, Collections.emptyList());
        OrMapper<Document> orMapper2 = entity.orMapper;
        assertEquals(orMapper, orMapper2);

        entity.mapToObject(createSimpleResultSet(), Collections.emptyList());
        orMapper2 = entity.orMapper;
        assertNotEquals(orMapper, orMapper2);
    }

}
