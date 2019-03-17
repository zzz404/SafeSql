package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

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
        if (value != null) {
            pstmt.setShort(index, value);
        }
        else {
            pstmt.setObject(index, null);
        }
        return this;
    }

}
