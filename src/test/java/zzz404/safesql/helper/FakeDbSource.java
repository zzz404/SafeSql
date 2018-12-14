package zzz404.safesql.helper;

import java.sql.SQLException;
import java.util.function.Function;

import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.EnhancedConnection;
import zzz404.safesql.util.CommonUtils;

public class FakeDbSource extends DbSourceImpl {

    private FakeDatabase fakeDb;

    public FakeDbSource(FakeDatabase db) {
        super("");
        this.fakeDb = db;
    }

    @Override
    public <T> T withConnection(Function<EnhancedConnection, T> func) {
        try {
            return func.apply(new EnhancedConnection(fakeDb.getMockedConnection()));
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

}
