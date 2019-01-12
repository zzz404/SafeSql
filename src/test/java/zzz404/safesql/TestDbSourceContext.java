package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.DbSourceImpl;

public class TestDbSourceContext {
    @BeforeEach
    void setUp() {
        DbSource.create().useConnectionPrivider(() -> mock(Connection.class));
    }

    @AfterEach
    void tearDown() {
        DbSource.clearAll();
    }

    @Test
    void test_withDbSource_nestCall_throwException() {
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

}
