package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class BooleanType extends ValueType<Boolean> {

    public Boolean readFromRs(QuietResultSet rs, int index) {
        return rs.getBoolean(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Boolean value) {
        pstmt.setBoolean(index, value);
    }

}
