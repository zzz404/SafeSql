package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class IntegerValue extends TypedValue<Integer> {

    @Override
    public IntegerValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getInt(index), rs);
        return this;
    }

    @Override
    public IntegerValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getInt(columnName), rs);
        return this;
    }

    @Override
    public IntegerValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        if (value != null) {
            pstmt.setInt(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
