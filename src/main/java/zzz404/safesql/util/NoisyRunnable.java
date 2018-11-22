package zzz404.safesql.util;

public interface NoisyRunnable {
    public void run() throws Exception;

    static boolean withoutException(NoisyRunnable runnable) {
        try {
            runnable.run();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static void runQuietly(NoisyRunnable runnable) {
        try {
            runnable.run();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    // public static Runnable shutUp(NoisyRunnable runnable) {
    // return () -> {
    // try {
    // runnable.run();
    // }
    // catch (Exception e) {
    // throw CommonUtils.wrapToRuntime(e);
    // }
    // };
    // }
}
