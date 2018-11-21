package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class DoubleType extends ValueType<Double> {

    @Override
    public Double readFromRs(QuietResultSet rs, int index) {
        return primitiveToObject(rs.getDouble(index), rs);
    }

    @Override
    public Double readFromRs(QuietResultSet rs, String columnName) {
        return primitiveToObject(rs.getDouble(columnName), rs);
    }

    @Override
    public void setToPstmt(QuietPreparedStatement pstmt, int index, Double value) {
        pstmt.setDouble(index, value);
    }

}
