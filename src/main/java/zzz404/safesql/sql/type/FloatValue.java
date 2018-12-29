package zzz404.safesql.sql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

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
        pstmt.setFloat(index, value);
        return this;
    }

}
