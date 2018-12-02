package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class BooleanType extends ValueType<Boolean> {

    public Boolean readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getBoolean(index), rs);
    }

    public Boolean readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getBoolean(columnName), rs);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Boolean value) {
        pstmt.setBoolean(index, value);
    }

}
