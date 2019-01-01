package zzz404.safesql.sql.type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class TimestampValue extends TypedValue<Timestamp> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public TimestampValue readFromRs(QuietResultSet rs, int index) {
        value = rs.getTimestamp(index);
        return this;
    }

    @Override
    public TimestampValue readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getTimestamp(columnName);
        return this;
    }

    @Override
    public TimestampValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTimestamp(index, value);
        return this;
    }

    @Override
    public String toValueString() {
        return DATE_TIME_FORMATTER.format(value);
    }
}
