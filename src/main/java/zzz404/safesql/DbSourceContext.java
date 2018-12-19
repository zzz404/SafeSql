package zzz404.safesql;

import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.DbSourceImpl;
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
        if (container.get() != null) {
            throw new IllegalStateException();
        }
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

}