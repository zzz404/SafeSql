package zzz404.safesql.type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TimestampType extends ValueType<Timestamp> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Timestamp readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index);
    }

    @Override
    public Timestamp readFromRs(QuietResultSet rs, String columnName) {
        return rs.getTimestamp(columnName);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Timestamp value)  {
        pstmt.setTimestamp(index, value);
    }

    @Override
    public String toString(Timestamp value) {
        return DATE_TIME_FORMATTER.format(value);
    }
}
