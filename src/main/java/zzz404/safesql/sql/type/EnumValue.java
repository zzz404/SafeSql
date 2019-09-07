package zzz404.safesql.sql.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class EnumValue<T extends Enum<T>> extends TypedValue<T> {

    public static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Class<T> clazz;

    public EnumValue(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public EnumValue<T> readFromRs(QuietResultSet rs, int index) {
        value = toEnum(rs.getString(index));
        return this;
    }

    private T toEnum(String s) {
        return s != null ? Enum.valueOf(clazz, s) : null;
    }

    @Override
    public EnumValue<T> readFromRs(QuietResultSet rs, String column) {
        value = toEnum(rs.getString(column));
        return this;
    }

    @Override
    public EnumValue<T> setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setString(index, value.name());
        return this;
    }

    @Override
    public String toValueString() {
        return DATE_TIME_FORMATTER.format(value.name());
    }

}
