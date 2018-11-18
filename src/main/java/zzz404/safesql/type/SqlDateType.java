package zzz404.safesql.type;

import java.sql.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class SqlDateType extends ValueType<Date> {

    public Date readFromRs(QuietResultSet rs, int index) {
        return rs.getDate(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Date value) {
        pstmt.setDate(index, value);
    }

}
