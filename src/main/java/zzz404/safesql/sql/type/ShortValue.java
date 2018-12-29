package zzz404.safesql.sql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class ShortValue extends TypedValue<Short> {

    @Override
    public ShortValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getShort(index), rs);
        return this;
    }

    @Override
    public ShortValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getShort(columnName), rs);
        return this;
    }

    @Override
    public ShortValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setShort(index, value);
        return this;
    }

}
