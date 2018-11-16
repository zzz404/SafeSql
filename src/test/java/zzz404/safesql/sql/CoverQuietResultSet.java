package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

public class CoverQuietResultSet {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.callAll(new QuietResultSet(null));

        ResultSet rs = mock(ResultSet.class);
        QuietResultSet qrs = new QuietResultSet(rs);
        UtilsForTest.callAll(qrs);
    }

}
