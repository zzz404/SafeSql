package zzz404.safesql.value;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class SqlDateValue extends TypedValue<Date> {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = rs.getDate(index);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getDate(columnName);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setDate(index, value);
    }

    @Override
    public String toValueString() {
        return DATE_FORMATTER.format(value);
    }

}
