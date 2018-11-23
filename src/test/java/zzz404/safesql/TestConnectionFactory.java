package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestConnectionFactory {

    @AfterEach
    public void tearDown() {
        ConnectionFactory.map.clear();
    }

    @Test
    public void test_create_then_get() {
        ConnectionFactory factory = ConnectionFactoryImpl.get("ds1");
        assertNull(factory);

        factory = ConnectionFactory.create("ds1", () -> null);
        assertNotNull(factory);

        ConnectionFactory factory2 = ConnectionFactoryImpl.get("ds1");
        assertTrue(factory == factory2);
    }

    @Test
    public void test_create_nameConflict_throwException() {
        ConnectionFactory.create("ds1", () -> null);
        ConfigException ex = assertThrows(ConfigException.class, () -> ConnectionFactory.create("ds1", () -> null));
        assertTrue(ex.getMessage().contains("conflict"));
        assertTrue(ex.getMessage().contains("ds1"));
    }

    @Test
    public void test_create_multipleInstance() {
        ConnectionFactory ds1 = ConnectionFactory.create("ds1", () -> null);
        ConnectionFactory ds2 = ConnectionFactory.create("ds2", () -> null);
        assertTrue(ds1 != ds2);
    }

}
