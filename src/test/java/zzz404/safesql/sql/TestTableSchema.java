package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.FakeSchemaBase;

public class TestTableSchema {

    @Test
    void test_initColumns_nameConflictWithSnake_throwException() throws SQLException {
        ResultSetMetaData metaData = FakeSchemaBase.mockMetaData("docTitle", "doc_title");
        TableSchema tableSchema = new TableSchema("", "", true);
        TableSchemeException e = assertThrows(TableSchemeException.class,
                () -> tableSchema.initColumns(new QuietResultSetMetaData(metaData)));
        assertTrue(e.getMessage().contains("ambiguous"));
    }

}
