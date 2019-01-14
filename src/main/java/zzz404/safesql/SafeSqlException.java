package zzz404.safesql;

@SuppressWarnings("serial")
public class SafeSqlException extends RuntimeException {

    public SafeSqlException() {
    }

    public SafeSqlException(String message) {
        super(message);
    }

    public SafeSqlException(Throwable cause) {
        super(cause);
    }

    public SafeSqlException(String message, Throwable cause) {
        super(message, cause);
    }

}
