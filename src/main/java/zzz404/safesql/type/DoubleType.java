package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class DoubleType extends ValueType<Double> {

    public Double readFromRs(QuietResultSet rs, int index) {
        return rs.getDouble(index);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, Double value) {
        pstmt.setDouble(index, value);
    }

}
