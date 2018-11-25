package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.QuietResultSet;

public class CoverQuietResultSet {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietResultSet(null));

        ResultSet rs = mock(ResultSet.class);
        QuietResultSet qrs = new QuietResultSet(rs);
        UtilsForTest.coverAll(qrs);
    }

}
