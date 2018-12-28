package zzz404.safesql.value;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class InstantValue extends TypedValue<Instant> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = toInstant(rs.getTimestamp(index));
    }

    private Instant toInstant(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        else {
            return timestamp.toInstant();
        }
    }

    @Override
    public void readFromRs(QuietResultSet rs, String column) {
        value = toInstant(rs.getTimestamp(column));
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTimestamp(index, Timestamp.from(value));
    }

    @Override
    public String toValueString() {
        return DATE_TIME_FORMATTER.format(Date.from(value));
    }

}
