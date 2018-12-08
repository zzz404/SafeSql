package zzz404.safesql.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.util.NoisyRunnable;

public class EnhancedConnection extends QuietConnection {
    QuietStatement stmt;
    Map<String, QuietPreparedStatement> map = new HashMap<>();

    public EnhancedConnection(Connection conn) {
        super(conn);
    }

    public void close() {
        if (stmt != null) {
            closeStatement(stmt);
        }
        for (QuietPreparedStatement pstmt : map.values()) {
            closeStatement(pstmt);
        }
        NoisyRunnable.runQuietly(() -> {
            conn.close();
        });
    }

    void closeStatement(Statement stmt) {
        try {
            stmt.close();
        }
        catch (Exception ignored) {
        }
    }

    public QuietStatement createStatement() {
        if (stmt == null) {
            stmt = new QuietStatement(super.createStatement());
        }
        return stmt;
    }

    public QuietStatement createStatement(int resultSetType, int resultSetConcurrency) {
        if (stmt == null) {
            stmt = new QuietStatement(super.createStatement(resultSetType, resultSetConcurrency));
        }
        else if (resultSetType > this.stmt.getResultSetType()
                || resultSetConcurrency > this.stmt.getResultSetConcurrency()) {
            NoisyRunnable.runIgnoreException(() -> stmt.close());
            resultSetType = Math.max(resultSetType, this.stmt.getResultSetType());
            resultSetConcurrency = Math.max(resultSetConcurrency, this.stmt.getResultSetConcurrency());
            stmt = new QuietStatement(super.createStatement(resultSetType, resultSetConcurrency));
        }
        return stmt;
    }

    public QuietPreparedStatement prepareStatement(String sql) {
        QuietPreparedStatement pstmt = map.get(sql);
        if (pstmt == null) {
            pstmt = new QuietPreparedStatement(super.prepareStatement(sql));
            map.put(sql, pstmt);
        }
        return pstmt;
    }

    public QuietPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        QuietPreparedStatement pstmt = map.get(sql);
        if (pstmt == null) {
            pstmt = new QuietPreparedStatement(super.prepareStatement(sql, resultSetType, resultSetConcurrency));
            map.put(sql, pstmt);
        }
        else if (resultSetType > pstmt.getResultSetType() || resultSetConcurrency > pstmt.getResultSetConcurrency()) {
            final QuietPreparedStatement pstmt2 = pstmt;
            NoisyRunnable.runIgnoreException(() -> pstmt2.close());
            resultSetType = Math.max(resultSetType, pstmt.getResultSetType());
            resultSetConcurrency = Math.max(resultSetConcurrency, pstmt.getResultSetConcurrency());
            pstmt = new QuietPreparedStatement(super.prepareStatement(sql, resultSetType, resultSetConcurrency));
            map.put(sql, pstmt);
        }
        return pstmt;
    }

}
