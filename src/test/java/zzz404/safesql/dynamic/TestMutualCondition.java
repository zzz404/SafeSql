package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Entity;
import zzz404.safesql.dynamic.Field;
import zzz404.safesql.dynamic.MutualCondition;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;

class TestMutualCondition {

    @Test
    void cover_rest() {
        Field<Object> field1 = new Field<>(new Entity<>(1, Document.class), "ownerId");
        Field<Object> field2 = new Field<>(new Entity<>(2, User.class), "id");
        MutualCondition<Object> cond = new MutualCondition<>(field1, "=", field2);
        cond.toString();
        MutualCondition<Object> cond2 = new MutualCondition<>(field1, ">", field2);
        assertNotEquals(cond, cond2);
    }

}
