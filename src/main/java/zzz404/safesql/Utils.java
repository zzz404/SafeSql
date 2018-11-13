package zzz404.safesql;

public final class Utils {
    private Utils() {
    }

    public static RuntimeException throwRuntime(Throwable e) {
        Utils.<RuntimeException> _throwRuntime(e);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void _throwRuntime(Throwable e)
            throws E {
        throw (E) e;
    }

}
