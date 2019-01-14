package zzz404.safesql.sql;

import zzz404.safesql.SafeSqlException;

@SuppressWarnings("serial")
public class TableNotFoundException extends SafeSqlException {

    public TableNotFoundException(Throwable cause, String... tables) {
        super("Cannot find table " + String.join(" or ", tables), cause);
    }

}
