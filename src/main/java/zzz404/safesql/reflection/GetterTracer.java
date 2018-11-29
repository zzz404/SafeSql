package zzz404.safesql.reflection;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zzz404.safesql.Entity;
import zzz404.safesql.QueryContext;
import zzz404.safesql.TableField;
import zzz404.safesql.util.NoisySupplier;

public class GetterTracer<T> implements MethodInterceptor {

    private ClassAnalyzer<T> classAnalyzer;
    private Entity<T> entity;

    public GetterTracer(Entity<T> entity) {
        this.classAnalyzer = ClassAnalyzer.get(entity.getObjClass());
        this.entity = entity;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        MethodAnalyzer m = classAnalyzer.getMethodAnalyzer(method);
        if (m.isGetter()) {
            QueryContext ctx = QueryContext.get();
            ctx.addTableField(new TableField(entity, m.getColumnName()));
        }
        return NoisySupplier.getQuietly(() -> proxy.invokeSuper(obj, args));
    }

}