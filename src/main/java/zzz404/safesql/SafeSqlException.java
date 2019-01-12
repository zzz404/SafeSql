package zzz404.safesql;

@SuppressWarnings("serial")
public class SafeSqlException extends RuntimeException {

    public SafeSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public SafeSqlException(String message) {
        super(message);
    }

}
