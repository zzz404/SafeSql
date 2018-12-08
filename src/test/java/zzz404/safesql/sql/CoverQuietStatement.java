package zzz404.safesql.sql;

import static org.mockito.Mockito.*;

import java.sql.Statement;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

public class CoverQuietStatement {

    @Test
    public void coverage_all() throws Exception {
        UtilsForTest.coverAll(new QuietStatement(null));

        Statement pstmt = mock(Statement.class);
        QuietStatement qstmt = new QuietStatement(pstmt);
        UtilsForTest.coverAll(qstmt);
    }

}
