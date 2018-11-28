package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestConnectionFactory {

    @AfterEach
    public void tearDown() {
        DbSource.map.clear();
    }

    @Test
    public void test_create_then_get() {
        DbSource factory = DbSource.get("ds1");
        assertNull(factory);

        factory = DbSource.create("ds1", () -> null);
        assertNotNull(factory);

        DbSource factory2 = DbSource.get("ds1");
        assertTrue(factory == factory2);
    }

    @Test
    public void test_create_nameConflict_throwException() {
        DbSource.create("ds1", () -> null);
        ConfigException ex = assertThrows(ConfigException.class, () -> DbSource.create("ds1", () -> null));
        assertTrue(ex.getMessage().contains("conflict"));
        assertTrue(ex.getMessage().contains("ds1"));
    }

    @Test
    public void test_create_multipleInstance() {
        DbSource ds1 = DbSource.create("ds1", () -> null);
        DbSource ds2 = DbSource.create("ds2", () -> null);
        assertTrue(ds1 != ds2);
    }

}
