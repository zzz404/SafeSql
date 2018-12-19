package zzz404.safesql.reflection;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zzz404.safesql.Entity;
import zzz404.safesql.EntityGettable;
import zzz404.safesql.Field;
import zzz404.safesql.QueryContext;
import zzz404.safesql.util.NoisySupplier;

public class GetterTracer<T> implements MethodInterceptor {

    private ClassAnalyzer<T> classAnalyzer;
    private Entity<T> entity;

    private static Method entityGetter = NoisySupplier.getQuietly(() -> EntityGettable.class.getMethod("entity"));

    public GetterTracer(Entity<T> entity) {
        this.classAnalyzer = ClassAnalyzer.get(entity.getObjClass());
        this.entity = entity;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        MethodAnalyzer m = classAnalyzer.getMethodAnalyzer(method);
        if (m == null && entityGetter.equals(method)) {
            return entity;
        }
        if (m.isGetter()) {
            QueryContext ctx = QueryContext.get();
            ctx.addTableField(new Field(entity, m.getPropertyName()));
        }
        return NoisySupplier.getQuietly(() -> proxy.invokeSuper(obj, args));
    }

}