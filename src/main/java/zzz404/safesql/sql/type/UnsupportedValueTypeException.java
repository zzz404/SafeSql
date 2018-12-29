package zzz404.safesql.sql.type;

public class UnsupportedValueTypeException extends RuntimeException {

    private static final long serialVersionUID = 3050447529361203247L;
    
    private Class<?> clazz;

    public UnsupportedValueTypeException(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return "Type " + clazz.getName() + " is not supported for mapping.";
    }

}
