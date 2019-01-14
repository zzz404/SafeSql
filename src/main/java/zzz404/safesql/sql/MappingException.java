package zzz404.safesql.sql;

import zzz404.safesql.SafeSqlException;

@SuppressWarnings("serial")
public class MappingException extends SafeSqlException {

    public MappingException(Class<?> clazz, String column) {
        super("Column " + column + " can not be set to " + clazz.getName());
    }

}
