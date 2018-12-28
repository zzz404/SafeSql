package zzz404.safesql.value;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class UtilDateValue extends TypedValue<Date> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = rs.getTimestamp(index);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getTimestamp(columnName);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTimestamp(index, new Timestamp(value.getTime()));
    }

    @Override
    public String toValueString() {
        return DATE_TIME_FORMATTER.format(value);
    }

}
