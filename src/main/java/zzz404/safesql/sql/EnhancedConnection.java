package zzz404.safesql.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import zzz404.safesql.util.CommonUtils;

public class EnhancedConnection extends QuietConnection {
    private QuietStatement stmt;
    private Map<String, QuietPreparedStatement> map = new HashMap<>();

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
        try {
            conn.close();
        }
        catch (Exception ignored) {
        }
    }

    void closeStatement(Statement stmt) {
        try {
            stmt.close();
        }
        catch (Exception ignored) {
        }
    }

    public QuietStatement createStatement() {
        try {
            if (stmt == null) {
                stmt = new QuietStatement(conn.createStatement());
                stmt.getResultSetType();
            }
            return stmt;
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public QuietStatement createStatement(int resultSetType, int resultSetConcurrency) {
        try {
            if (stmt == null) {
                stmt = new QuietStatement(conn.createStatement(resultSetType, resultSetConcurrency));
                stmt.getResultSetType();
            }
            return stmt;
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public QuietPreparedStatement prepareStatement(String sql) {
        try {
            QuietPreparedStatement pstmt = map.get(sql);
            if (pstmt == null) {
                pstmt = new QuietPreparedStatement(conn.prepareStatement(sql));
                map.put(sql, pstmt);
            }
            return pstmt;
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public QuietPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        try {
            QuietPreparedStatement pstmt = map.get(sql);
            if (pstmt == null) {
                pstmt = new QuietPreparedStatement(conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
                map.put(sql, pstmt);
            }
            return pstmt;
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

}
