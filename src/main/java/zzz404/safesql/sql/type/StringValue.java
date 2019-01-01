package zzz404.safesql.sql.type;

import zzz404.safesql.sql.proxy.QuietPreparedStatement;
import zzz404.safesql.sql.proxy.QuietResultSet;

public class StringValue extends TypedValue<String> {

    @Override
    public StringValue readFromRs(QuietResultSet rs, int index) {
        value = primitiveToObject(rs.getString(index), rs);
        return this;
    }

    @Override
    public StringValue readFromRs(QuietResultSet rs, String columnName) {
        value = primitiveToObject(rs.getString(columnName), rs);
        return this;
    }

    @Override
    public StringValue setToPstmt(QuietPreparedStatement pstmt, int index) {
        pstmt.setString(index, value);
        return this;
    }

    @Override
    public String toValueString() {
        return "'" + value + "'";
    }

}
