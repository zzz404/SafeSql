package zzz404.safesql;

public final class Utils {
    private Utils() {
    }

    public static RuntimeException throwRuntime(Throwable e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        else {
            throw new RuntimeException(e);
        }
    }
}
