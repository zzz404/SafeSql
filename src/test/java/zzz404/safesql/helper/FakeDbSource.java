package zzz404.safesql.helper;

import java.util.function.Function;

import zzz404.safesql.DbSourceImpl;
import zzz404.safesql.sql.EnhancedConnection;

public class FakeDbSource extends DbSourceImpl {

    private FakeDatabase fakeDb;

    public FakeDbSource(FakeDatabase db) {
        super("");
        this.fakeDb = db;
    }

    @Override
    public <T> T withConnection(Function<EnhancedConnection, T> func) {
        return func.apply(new EnhancedConnection(fakeDb.getConnection()));
    }

}
