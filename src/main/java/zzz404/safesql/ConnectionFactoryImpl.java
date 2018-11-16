package zzz404.safesql;

import zzz404.safesql.sql.QuietConnection;

public class ConnectionFactoryImpl extends ConnectionFactory {

    public QuietConnection getQuietConnection() {
        return new QuietConnection(connectionProvider.getConnection());
    }

}
