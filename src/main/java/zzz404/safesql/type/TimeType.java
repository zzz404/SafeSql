package zzz404.safesql.type;

import java.sql.Time;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TimeType extends ValueType<Time> {

    public Time readFromRs(QuietResultSet rs, int index) {
        return rs.getTime(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Time value) {
        pstmt.setTime(index, value);
    }

}
