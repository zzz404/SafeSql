package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.Scope;
import zzz404.safesql.dynamic.ScopeErrorException;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeSchemaBase;

public class TestScope {

    @BeforeEach
    void setUp() {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());
    }

    @AfterEach
    void afterEach() {
        DbSource.clearAll();
    }

    @Test
    void test_checkCommand() {
        ScopeErrorException e = assertThrows(ScopeErrorException.class, () -> {
            from(Document.class).select(d -> {
                asc(d.getId());
            });
        });
        assertTrue(e.getMessage().contains("asc"));
        assertTrue(e.getMessage().contains(Scope.select.name()));
    }
}
