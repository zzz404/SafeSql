package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.QuietConnection;

public class CoverQuietConnection {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietConnection(null));

        Connection rs = mock(Connection.class);
        QuietConnection qrs = new QuietConnection(rs);
        UtilsForTest.coverAll(qrs);
    }

}
