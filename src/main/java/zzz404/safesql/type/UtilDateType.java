package zzz404.safesql.type;

import java.sql.Timestamp;
import java.util.Date;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class UtilDateType extends ValueType<Date> {

    public Date readFromRs(QuietResultSet rs, int index) {
        return rs.getTimestamp(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Date value) {
        pstmt.setTimestamp(index, new Timestamp(value.getTime()));
    }

}
