package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class LongType extends ValueType<Long> {

    public Long readFromRs(QuietResultSet rs, int index) {
        return rs.getLong(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Long value) {
        pstmt.setLong(index, value);
    }

}
