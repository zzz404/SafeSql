package zzz404.safesql.value;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TimeValue extends TypedValue<Time> {

    public static final DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = rs.getTime(index);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getTime(columnName);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTime(index, value);
    }

    @Override
    public String toValueString() {
        return TIME_FORMATTER.format(value);
    }

}
