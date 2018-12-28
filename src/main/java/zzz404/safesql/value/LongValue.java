package zzz404.safesql.value;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class LongValue extends TypedValue<Long> {

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getLong(index), rs);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getLong(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setLong(index, value);
    }

}
