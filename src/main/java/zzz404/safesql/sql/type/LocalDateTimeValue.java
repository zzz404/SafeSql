package zzz404.safesql.sql.type;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class LocalDateTimeValue extends TypedValue<LocalDateTime> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTimeValue readFromRs(QuietResultSet rs, int index) {
        value = toLocalDateTime(rs.getTimestamp(index));
        return this;
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    @Override
    public LocalDateTimeValue readFromRs(QuietResultSet rs, String column) {
        value = toLocalDateTime(rs.getTimestamp(column));
        return this;
    }

    @Override
    public LocalDateTimeValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setTimestamp(index, Timestamp.valueOf(value));
        return this;
    }

    @Override
    public String toValueString() {
        return value.format(DATE_TIME_FORMATTER);
    }

}
