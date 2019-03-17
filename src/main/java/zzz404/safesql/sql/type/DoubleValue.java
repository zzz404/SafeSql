package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class DoubleValue extends TypedValue<Double> {

    @Override
    public DoubleValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getDouble(index), rs);
        return this;
    }

    @Override
    public DoubleValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getDouble(columnName), rs);
        return this;
    }

    @Override
    public DoubleValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        if (value != null) {
            pstmt.setDouble(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
