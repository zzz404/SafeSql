package zzz404.safesql.type;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class SqlDateType extends ValueType<Date> {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date readFromRs(QuietResultSet rs, int index) {
        return rs.getDate(index);
    }

    @Override
    public Date readFromRs(QuietResultSet rs, String columnName) {
        return rs.getDate(columnName);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Date value) {
        pstmt.setDate(index, value);
    }

    @Override
    public String toString(Date value) {
        return DATE_FORMATTER.format(value);
    }

}
