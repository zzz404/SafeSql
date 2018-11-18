package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class IntegerType extends ValueType<Integer> {

    public Integer readFromRs(QuietResultSet rs, int index) {
        return rs.getInt(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Integer value) {
        pstmt.setInt(index, value);
    }

}
