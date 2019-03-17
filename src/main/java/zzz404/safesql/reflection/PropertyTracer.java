package zzz404.safesql.reflection;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zzz404.safesql.dynamic.Entity;
import zzz404.safesql.dynamic.EntityGettable;
import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.QueryContext;
import zzz404.safesql.util.NoisySupplier;

public class PropertyTracer<T> implements MethodInterceptor {

    private ClassAnalyzer classAnalyzer;
    private Entity<T> entity;
    private boolean traceGetter;
    private boolean traceSetter;

    private static Method entityGetter = NoisySupplier.getQuietly(() -> EntityGettable.class.getMethod("entity"));

    public PropertyTracer(Entity<T> entity, boolean traceGetter, boolean traceSetter) {
        this.classAnalyzer = ClassAnalyzer.get(entity.getObjClass());
        this.entity = entity;
        this.traceGetter = traceGetter;
        this.traceSetter = traceSetter;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        MethodAnalyzer m = classAnalyzer.getMethodAnalyzer(method);
        if (m == null && entityGetter.equals(method)) {
            return entity;
        }
        if (traceGetter && m.isGetter()) {
            QueryContext ctx = QueryContext.get();
            ctx.addTableField(new FieldImpl(entity, m.getPropertyName(), m.getType()));
        }
        if (traceSetter && m.isSetter()) {
            QueryContext ctx = QueryContext.get();
            FieldImpl field = new FieldImpl(entity, m.getPropertyName(), m.getType());
            field.setValue(args[0]);
            ctx.addTableField(field);
        }
        return NoisySupplier.getQuietly(() -> proxy.invokeSuper(obj, args));
    }

}