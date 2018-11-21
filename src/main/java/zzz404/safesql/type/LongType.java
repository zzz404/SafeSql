package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class LongType extends ValueType<Long> {

    public Long readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getLong(index), rs);
    }

    @Override
    public Long readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getLong(columnName), rs);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Long value)  {
        pstmt.setLong(index, value);
    }

}
