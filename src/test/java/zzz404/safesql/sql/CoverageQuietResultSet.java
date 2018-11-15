package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.TestUtils;

public class CoverageQuietResultSet {

    @Test
    public void coverage_all() throws Exception {
        TestUtils.callAll(new QuietResultSet(null));

        ResultSet rs = mock(ResultSet.class);
        QuietResultSet qrs = new QuietResultSet(rs);
        TestUtils.callAll(qrs);
    }

}
