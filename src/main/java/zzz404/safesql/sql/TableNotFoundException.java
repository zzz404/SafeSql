package zzz404.safesql.sql;

public class TableNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2708234246921318821L;
    private String[] tables;

    public TableNotFoundException(Throwable cause, String... tables) {
        super(cause);
        this.tables = tables;
    }

    @Override
    public String getMessage() {
        return "Cannot find table " + String.join(" or ", tables);
    }

}
