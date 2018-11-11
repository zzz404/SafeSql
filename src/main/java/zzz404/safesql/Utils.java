package zzz404.safesql;

public final class Utils {
    private Utils() {
    }

    public static RuntimeException throwRuntime(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        else {
            throw new RuntimeException(t);
        }
    }
}
