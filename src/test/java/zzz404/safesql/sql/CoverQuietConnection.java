package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

public class CoverQuietConnection {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.callAll(new QuietConnection(null));

        Connection rs = mock(Connection.class);
        QuietConnection qrs = new QuietConnection(rs);
        UtilsForTest.callAll(qrs);
    }

}
