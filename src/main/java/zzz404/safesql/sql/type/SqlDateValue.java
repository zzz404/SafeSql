package zzz404.safesql.sql.type;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class SqlDateValue extends TypedValue<Date> {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public SqlDateValue readFromRs(QuietResultSet rs, int index) {
        value = rs.getDate(index);
        return this;
    }

    @Override
    public SqlDateValue readFromRs(QuietResultSet rs, String columnName) {
        value = rs.getDate(columnName);
        return this;
    }

    @Override
    public SqlDateValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setDate(index, value);
        return this;
    }

    @Override
    public String toValueString() {
        return DATE_FORMATTER.format(value);
    }

}
