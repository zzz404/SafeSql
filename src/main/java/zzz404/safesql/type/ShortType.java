package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class ShortType extends ValueType<Short> {

    public Short readFromRs(QuietResultSet rs, int index) {
        return rs.getShort(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Short value) {
        pstmt.setShort(index, value);
    }

}
