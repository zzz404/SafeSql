package zzz404.safesql.sql;

import zzz404.safesql.SafeSqlException;

@SuppressWarnings("serial")
public class ColumnNotFoundException extends SafeSqlException {

    public ColumnNotFoundException(String columnName, String tableName) {
        super("Cannot find column " + columnName + " at table " + tableName);
    }

}
