package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestDbSourceImpl {

    @AfterEach
    public void tearDown() {
        DbSource.map.clear();
    }

    @Test
    public void test_create_then_get() {
        assertThrows(NullPointerException.class, () -> DbSource.get("ds1"));

        DbSource factory = DbSource.create("ds1");
        assertNotNull(factory);

        DbSource factory2 = DbSource.get("ds1");
        assertTrue(factory == factory2);
    }

    @Test
    public void test_create_nameConflict_throwException() {
        DbSource.create("ds1");
        ConfigException ex = assertThrows(ConfigException.class, () -> DbSource.create("ds1"));
        assertTrue(ex.getMessage().contains("conflict"));
        assertTrue(ex.getMessage().contains("ds1"));
    }

    @Test
    public void test_create_multipleInstance() {
        DbSource ds1 = DbSource.create("ds1");
        DbSource ds2 = DbSource.create("ds2");
        assertTrue(ds1 != ds2);
    }

}
