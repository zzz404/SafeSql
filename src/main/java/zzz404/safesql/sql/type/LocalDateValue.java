package zzz404.safesql.sql.type;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class LocalDateValue extends TypedValue<LocalDate> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDateValue readFromRs(QuietResultSet rs, int index) {
        value = toLocalDate(rs.getDate(index));
        return this;
    }

    private LocalDate toLocalDate(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        else {
            return date.toLocalDate();
        }
    }

    @Override
    public LocalDateValue readFromRs(QuietResultSet rs, String column) {
        value = toLocalDate(rs.getDate(column));
        return this;
    }

    @Override
    public LocalDateValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setDate(index, java.sql.Date.valueOf(value));
        return this;
    }

    @Override
    public String toValueString() {
        return value.format(DATE_TIME_FORMATTER);
    }

}
