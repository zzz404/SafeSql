package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.TestUtils;

public class CoverQuietPreparedStatement {

    @Test
    public void coverage_all() throws Exception {
        TestUtils.callAll(new QuietPreparedStatement(null));

        PreparedStatement pstmt = mock(PreparedStatement.class);
        QuietPreparedStatement qpstmt = new QuietPreparedStatement(pstmt);
        TestUtils.callAll(qpstmt);
    }

}
