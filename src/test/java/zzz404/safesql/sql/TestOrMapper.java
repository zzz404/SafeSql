package zzz404.safesql.sql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import zzz404.safesql.ConnFactoryBackDoor;
import zzz404.safesql.helper.FakeDatabase;

public class TestOrMapper {
    private FakeDatabase fakeDb;

    @BeforeEach
    void beforeEach() {
        fakeDb = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        ConnFactoryBackDoor.removeAllFactories();
    }

    void test_mapToObject() {
        FakeDatabase fakeDb;

    }
}
