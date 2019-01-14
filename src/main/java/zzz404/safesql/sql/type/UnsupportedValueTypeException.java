package zzz404.safesql.sql.type;

import zzz404.safesql.SafeSqlException;

@SuppressWarnings("serial")
public class UnsupportedValueTypeException extends SafeSqlException {
    
    private Class<?> clazz;

    public UnsupportedValueTypeException(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return "Type " + clazz.getName() + " is not supported for mapping.";
    }

}
