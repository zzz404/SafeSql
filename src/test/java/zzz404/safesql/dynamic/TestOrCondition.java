package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.SQLException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.User;
import zzz404.safesql.sql.type.TypedValue;

class TestOrCondition {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class, User.class, Category.class).where((d, u, c) -> {
            cond(d.getOwnerId(), "=", u.getId()).or(d.getId(), "=", 123).or(c.getName(), LIKE, "%z%");
        });
        assertEquals("SELECT * FROM Document t1, User t2, Category t3"
                + " WHERE (t1.ownerId = t2.id OR t1.id = ? OR t3.name LIKE ?)", q.sql());
        assertEquals(Arrays.asList(TypedValue.valueOf(123), TypedValue.valueOf("%z%")), q.paramValues());
    }

}
