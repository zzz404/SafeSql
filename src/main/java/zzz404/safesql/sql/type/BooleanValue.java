package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class BooleanValue extends TypedValue<Boolean> {

    @Override
    public BooleanValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getBoolean(index), rs);
        return this;
    }

    @Override
    public BooleanValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getBoolean(columnName), rs);
        return this;
    }

    @Override
    public BooleanValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        if (value != null) {
            pstmt.setBoolean(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
