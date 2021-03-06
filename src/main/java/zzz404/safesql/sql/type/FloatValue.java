package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class FloatValue extends TypedValue<Float> {

    @Override
    public FloatValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getFloat(index), rs);
        return this;
    }

    @Override
    public FloatValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getFloat(columnName), rs);
        return this;
    }

    @Override
    public FloatValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        if (value != null) {
            pstmt.setFloat(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
