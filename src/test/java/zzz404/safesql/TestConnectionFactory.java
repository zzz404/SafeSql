package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestConnectionFactory {

    @AfterEach
    public void tearDown() {
        ConnectionFactory.clearAll();
    }

    @Test
    public void test_create_then_get() {
        ConnectionFactory factory = ConnectionFactory.get("ds1");
        assertNull(factory);

        factory = ConnectionFactory.create("ds1");
        assertNotNull(factory);

        ConnectionFactory factory2 = ConnectionFactory.get("ds1");
        assertTrue(factory == factory2);
    }

    @Test
    public void test_create_nameConflict_throwException() {
        ConnectionFactory.create("ds1");
        ConfigException ex = assertThrows(ConfigException.class,
                () -> ConnectionFactory.create("ds1"));
        assertTrue(ex.getMessage().contains("conflict"));
        assertTrue(ex.getMessage().contains("ds1"));
    }

    @Test
    public void test_create_multipleInstance() {
        ConnectionFactory ds1 = ConnectionFactory.create("ds1");
        ConnectionFactory ds2 = ConnectionFactory.create("ds2");
        assertTrue(ds1 != ds2);
    }

}
