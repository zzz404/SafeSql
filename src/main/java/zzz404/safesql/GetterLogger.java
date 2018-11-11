package zzz404.safesql;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class GetterLogger<T> implements MethodInterceptor {

    private ClassAnalyzer<T> classAnalyzer;

    public GetterLogger(Class<T> clazz) {
        this.classAnalyzer = ClassAnalyzer.get(clazz);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args,
            MethodProxy proxy) {

        MethodAnalyzer m = classAnalyzer.getMethodAnalyzer(method);
        if (m.isGetter()) {
            QueryContext ctx = QueryContext.INSTANCE.get();
            ctx.addColumnName(m.getPropertyName());
        }
        try {
            return proxy.invokeSuper(obj, args);
        }
        catch (Throwable e) {
            throw Utils.throwRuntime(e);
        }
    }

}