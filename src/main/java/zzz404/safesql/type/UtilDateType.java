package zzz404.safesql.type;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class UtilDateType extends ValueType<Date> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Date readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index);
    }

    @Override
    public Date readFromRs(QuietResultSet rs, String columnName) {
        return rs.getTimestamp(columnName);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Date value)  {
        pstmt.setTimestamp(index, new Timestamp(value.getTime()));
    }

    @Override
    public String toString(Date value) {
        return DATE_TIME_FORMATTER.format(value);
    }

}
