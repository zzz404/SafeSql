package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import zzz404.safesql.sql.DbSourceImpl;

public class TestDbSourceContext {
    @AfterEach
    void tearDown() {
        DbSource.map.clear();
    }

    @Test
    void test_withDbSource_nestCall_throwException() {
        Connection conn = Mockito.mock(Connection.class);
        DbSource.create().useConnectionPrivider(() -> conn);
        DbSourceImpl ds = DbSourceImpl.get("");
        assertThrows(IllegalStateException.class, () -> {
            DbSourceContext.withDbSource(ds, () -> {
                DbSourceContext.withDbSource(ds, () -> {
                    return null;
                });
                return null;
            });
        });
    }

    @Test
    void test_withConnection_checkDataSource() {
        Connection conn = Mockito.mock(Connection.class);
        DbSource.create().useConnectionPrivider(() -> conn);
        DbSourceImpl ds = DbSourceImpl.get("");

        DbSource.create("z").useConnectionPrivider(() -> conn);

        assertThrows(IllegalArgumentException.class, () -> {
            use("z").withTheSameConnection(() -> {
                DbSourceContext.withConnection(ds, enConn -> null);
                return null;
            });
        });
    }
}
