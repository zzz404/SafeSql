package zzz404.safesql.type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class InstantType extends ValueType<Instant> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Instant readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index).toInstant();
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Instant value) {
        pstmt.setTimestamp(index, Timestamp.from(value));
    }

    @Override
    public String toString(Instant value) {
        return DATE_TIME_FORMATTER.format(Date.from(value));
    }

}
