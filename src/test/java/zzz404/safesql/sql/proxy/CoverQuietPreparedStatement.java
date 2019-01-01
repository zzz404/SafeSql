package zzz404.safesql.sql.proxy;

import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.proxy.QuietPreparedStatement;

public class CoverQuietPreparedStatement {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietPreparedStatement(null));

        PreparedStatement pstmt = mock(PreparedStatement.class);
        QuietPreparedStatement qpstmt = new QuietPreparedStatement(pstmt);
        UtilsForTest.coverAll(qpstmt);
    }

}
