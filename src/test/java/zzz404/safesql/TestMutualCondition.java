package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;

class TestMutualCondition {

    @Test
    void coverRest() {
        Field field1 = new Field(new Entity<>(1, Document.class), "ownerId");
        Field field2 = new Field(new Entity<>(2, User.class), "id");
        MutualCondition cond = new MutualCondition(field1, "=", field2);
        cond.toString();
        MutualCondition cond2 = new MutualCondition(field1, ">", field2);
        assertNotEquals(cond, cond2);
    }

}
