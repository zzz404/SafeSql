package zzz404.safesql.sql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

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
        pstmt.setInt(index, value);
        return this;
    }

}
