package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

class TestSql {

    @Test
    void test() {
        from(Document.class, User.class).select((d, u) -> {
            d.getId();
            d.getTitle();
            u.getName();
        }).where((d, u) -> {
            equal(d.getOwnerId(), u.getId());
        }).orderBy((d, u) -> {
            asc(u.getName());
            asc(d.getTitle());
        }).offset(20).limit(10).queryStream();

    }

}
