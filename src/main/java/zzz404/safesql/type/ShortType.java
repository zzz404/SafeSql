package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class ShortType extends ValueType<Short> {

    public Short readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getShort(index), rs);
    }

    @Override
    public Short readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getShort(columnName), rs);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Short value)  {
        pstmt.setShort(index, value);
    }

}
