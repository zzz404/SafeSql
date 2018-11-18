package zzz404.safesql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import zzz404.safesql.helper.FakeDatabase;

class TestEntityQuerier2_Query {

    private FakeDatabase packet;
    
    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create().setConnectionPrivider(() -> packet.getConnection());
        QueryContext.create("");
        packet = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        QueryContext.clear();
        ConnectionFactory.map.clear();
    }

}
