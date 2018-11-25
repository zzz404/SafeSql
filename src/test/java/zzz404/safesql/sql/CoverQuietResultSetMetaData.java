package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.QuietResultSetMetaData;

public class CoverQuietResultSetMetaData {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietResultSetMetaData(null));

        ResultSetMetaData rs = mock(ResultSetMetaData.class);
        QuietResultSetMetaData qrs = new QuietResultSetMetaData(rs);
        UtilsForTest.coverAll(qrs);
    }

}
