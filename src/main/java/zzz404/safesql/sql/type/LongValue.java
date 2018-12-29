package zzz404.safesql.sql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class LongValue extends TypedValue<Long> {

    @Override
    public LongValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getLong(index), rs);
        return this;
    }

    @Override
    public LongValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getLong(columnName), rs);
        return this;
    }

    @Override
    public LongValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setLong(index, value);
        return this;
    }

}
