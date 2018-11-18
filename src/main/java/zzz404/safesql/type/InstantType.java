package zzz404.safesql.type;

import java.sql.Timestamp;
import java.time.Instant;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class InstantType extends ValueType<Instant> {

    public Instant readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index).toInstant();
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Instant value) {
        pstmt.setTimestamp(index, Timestamp.from(value));
    }

}
