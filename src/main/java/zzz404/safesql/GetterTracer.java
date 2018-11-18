package zzz404.safesql;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zzz404.safesql.util.NoisySupplier;

class GetterTracer<T> implements MethodInterceptor {

    private ClassAnalyzer<T> classAnalyzer;
    private int tableIndex;

    public GetterTracer(Class<T> clazz, int tableIndex) {
        this.classAnalyzer = ClassAnalyzer.get(clazz);
        this.tableIndex = tableIndex;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        MethodAnalyzer m = classAnalyzer.getMethodAnalyzer(method);
        if (m.isGetter()) {
            QueryContext ctx = QueryContext.get();
            ctx.addTableColumn(tableIndex, m.getColumnName());
        }
        return NoisySupplier.getQuiet(() -> proxy.invokeSuper(obj, args));
    }

}