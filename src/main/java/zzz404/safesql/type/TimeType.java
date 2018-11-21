package zzz404.safesql.type;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TimeType extends ValueType<Time> {

    public static final DateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    @Override
    public Time readFromRs(QuietResultSet rs, int index) {
        return rs.getTime(index);
    }

    @Override
    public Time readFromRs(QuietResultSet rs, String columnName) {
        return rs.getTime(columnName);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index, Time value)  {
        pstmt.setTime(index, value);
    }

    @Override
    public String toString(Time value) {
        return TIME_FORMATTER.format(value);
    }

}
