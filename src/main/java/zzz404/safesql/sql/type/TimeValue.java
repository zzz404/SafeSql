package zzz404.safesql.sql.type;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class TimeValue extends TypedValue<Time> {

    public static final DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    @Override
    public TimeValue readFromRs(QuietResultSet rs, int index) {
        value = rs.getTime(index);
        return this;
    }

    @Override
    public TimeValue readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getTime(columnName);
        return this;
    }

    @Override
    public TimeValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTime(index, value);
        return this;
    }

    @Override
    public String toValueString() {
        return TIME_FORMATTER.format(value);
    }

}
