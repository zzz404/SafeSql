package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

public class CoverQuietPreparedStatement {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietPreparedStatement(null));

        PreparedStatement pstmt = mock(PreparedStatement.class);
        QuietPreparedStatement qpstmt = new QuietPreparedStatement(pstmt);
        UtilsForTest.coverAll(qpstmt);
    }

}
