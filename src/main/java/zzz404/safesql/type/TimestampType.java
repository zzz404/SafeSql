package zzz404.safesql.type;

import java.sql.Timestamp;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TimestampType extends ValueType<Timestamp> {

    public Timestamp readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Timestamp value) {
        pstmt.setTimestamp(index, value);
    }

}
