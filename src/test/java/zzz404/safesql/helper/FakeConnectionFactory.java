package zzz404.safesql.helper;

import zzz404.safesql.sql.ConnectionFactoryImpl;
import zzz404.safesql.sql.QuietConnection;

public class FakeConnectionFactory extends ConnectionFactoryImpl {

    private FakeDatabase fakeDb;

    public FakeConnectionFactory(FakeDatabase db) {
        super("");
        this.fakeDb = db;
    }

    @Override
    public QuietConnection getQuietConnection() {
        return new QuietConnection(fakeDb.getConnection());
    }

}
