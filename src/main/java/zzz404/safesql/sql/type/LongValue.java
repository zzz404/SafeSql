package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

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
        if (value != null) {
            pstmt.setLong(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
