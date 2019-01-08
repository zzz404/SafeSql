package zzz404.safesql.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.sf.cglib.proxy.Enhancer;
import zzz404.safesql.reflection.GetterTracer;
import zzz404.safesql.util.CommonUtils;

public class Entity<T> {
    private int index;
    private Class<T> objClass;
    private T mockedObject;

    private List<FieldImpl<?>> fields = new ArrayList<>();

    public Entity(int index, Class<T> clazz) {
        this.index = index;
        this.objClass = clazz;
        this.mockedObject = createMockedObject(clazz, index);
    }

    private T createMockedObject(Class<T> clazz, int index) {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        en.setInterfaces(new Class[] {EntityGettable.class});
        GetterTracer<T> getterLogger = new GetterTracer<>(this);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    public String getName() {
        return objClass.getSimpleName();
    }

    public void addField(FieldImpl<?> field) {
        fields.add(field);
    }

    public List<FieldImpl<?>> getFields() {
        return fields;
    }

    public int getIndex() {
        return index;
    }

    public Class<T> getObjClass() {
        return objClass;
    }

    public T getMockedObject() {
        return mockedObject;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, tc -> new Object[] { tc.index, tc.objClass });
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, objClass);
    }

}
