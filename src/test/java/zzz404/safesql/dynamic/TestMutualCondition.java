package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.User;

class TestMutualCondition {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }
    
    @Test
    void test_sql_and_paramValues() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class, User.class).where((d, u) -> {
            cond(d.getOwnerId(), "<>", u.getId());
        });
        assertEquals("SELECT * FROM Document t1, User t2 WHERE t1.ownerId <> t2.id", q.sql());
        assertEquals(Collections.emptyList(), q.paramValues());
    }

}
