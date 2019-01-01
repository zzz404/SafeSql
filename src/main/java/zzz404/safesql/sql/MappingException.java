package zzz404.safesql.sql;

public class MappingException extends RuntimeException {
    private static final long serialVersionUID = 332330665663323758L;

    public MappingException(Class<?> clazz, String column) {
        super("Column " + column + " can not be set to " + clazz.getName());
    }

}
