package zzz404.safesql.util;

public interface NoisyRunnable {
    public void run() throws Exception;

    public static void runQuiet(NoisyRunnable runnable) {
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
