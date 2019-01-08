package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.ScopeErrorException;
import zzz404.safesql.helper.Document;

public class TestScope {

    @AfterEach
    void afterEach() {
        DbSource.map.clear();
    }

    @Test
    void test_checkCommand() {
        DbSource.create();

        assertThrows(ScopeErrorException.class, () -> {
            from(Document.class).select(d -> {
                asc(d.getId());
            });
        });
    }
}
