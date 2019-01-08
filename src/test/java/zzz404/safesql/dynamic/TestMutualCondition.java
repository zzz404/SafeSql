package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.MutualCondition;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.User;

class TestMutualCondition {

    @Test
    void cover_rest() {
        FieldImpl<Object> field1 = new FieldImpl<>(new Entity<>(1, Document.class), "ownerId");
        FieldImpl<Object> field2 = new FieldImpl<>(new Entity<>(2, User.class), "id");
        MutualCondition<Object> cond = new MutualCondition<>(field1, "=", field2);
        cond.toString();
        MutualCondition<Object> cond2 = new MutualCondition<>(field1, ">", field2);
        assertNotEquals(cond, cond2);
    }

}
