package zzz404.safesql.value;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class ShortValue extends TypedValue<Short> {

    @Override
    public void readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getShort(index), rs);
    }

    @Override
    public void readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getShort(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setShort(index, value);
    }

}
