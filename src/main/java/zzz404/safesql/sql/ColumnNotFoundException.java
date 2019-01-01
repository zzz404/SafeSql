package zzz404.safesql.sql;

public class ColumnNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2248975206375233499L;

    public ColumnNotFoundException(String columnName, String tableName) {
        super("Cannot find column " + columnName + " at table " + tableName);
    }

}
