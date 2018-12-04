package zzz404.safesql;

import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;
import zzz404.safesql.querier.ThreeEntityQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.EnhancedConnection;

public class DbSourceContext {

    private static final ThreadLocal<DbSourceContext> container = new ThreadLocal<>();

    DbSourceImpl dbSource;
    private EnhancedConnection conn;

    public DbSourceContext(DbSourceImpl dbSource) {
        this.dbSource = dbSource;
    }

    public static <T> T withConnection(DbSourceImpl dbSource, Function<EnhancedConnection, T> func) {
        DbSourceContext ctx = get();
        if (ctx == null) {
            return dbSource.withConnection(func);
        }
        else {
            Validate.isTrue(dbSource == ctx.dbSource);
            return func.apply(ctx.conn);
        }
    }

    static <T> T withDbSource(DbSourceImpl dbSource, Supplier<T> supplier) {
        Validate.isTrue(container.get() == null);
        DbSourceContext ctx = new DbSourceContext(dbSource);
        return ctx.dbSource.withConnection(conn -> {
            try {
                container.set(ctx);
                ctx.conn = conn;
                return supplier.get();
            }
            finally {
                ctx.conn.close();
                container.remove();
            }
        });
    }

    static DbSourceContext get() {
        return container.get();
    }

    public StaticSqlQuerier sql(String sql) {
        return new StaticSqlQuerier(dbSource).sql(sql);
    }

    public <T> OneEntityQuerier<T> from(Class<T> clazz) {
        return new OneEntityQuerier<>(dbSource, clazz);
    }

    public <T, U> TwoEntityQuerier<T, U> from(Class<T> class1, Class<U> class2) {
        return new TwoEntityQuerier<>(dbSource, class1, class2);
    }

    public <T, U, V> ThreeEntityQuerier<T, U, V> from(Class<T> class1, Class<U> class2, Class<V> class3) {
        return new ThreeEntityQuerier<>(dbSource, class1, class2, class3);
    }
}