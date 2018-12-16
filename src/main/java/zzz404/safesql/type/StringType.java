package zzz404.safesql.type;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class StringType extends ValueType<String> {

    public String readFromRs(QuietResultSet rs, int index) {
        return rs.getString(index);
    }

    @Override
    public String readFromRs(QuietResultSet rs, String columnName) {
        return rs.getString(columnName);
    }

    public void setToPstmt(QuietPreparedStatement pstmt, int index, String value) {
        pstmt.setString(index, value);
    }

    public String toString(String value) {
        return "'" + value + "'";
    }

}
