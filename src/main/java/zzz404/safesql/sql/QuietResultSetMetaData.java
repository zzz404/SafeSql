package zzz404.safesql.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import zzz404.safesql.CommonUtils;

public class QuietResultSetMetaData implements ResultSetMetaData {
    private ResultSetMetaData meta;

    public QuietResultSetMetaData(ResultSetMetaData meta) {
        this.meta = meta;
    }

    public <T> T unwrap(Class<T> iface) {
        try {
            return meta.unwrap(iface);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getColumnCount() {
        try {
            return meta.getColumnCount();
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isAutoIncrement(int column) {
        try {
            return meta.isAutoIncrement(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isCaseSensitive(int column) {
        try {
            return meta.isCaseSensitive(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isSearchable(int column) {
        try {
            return meta.isSearchable(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWrapperFor(Class<?> iface) {
        try {
            return meta.isWrapperFor(iface);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isCurrency(int column) {
        try {
            return meta.isCurrency(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int isNullable(int column) {
        try {
            return meta.isNullable(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isSigned(int column) {
        try {
            return meta.isSigned(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getColumnDisplaySize(int column) {
        try {
            return meta.getColumnDisplaySize(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getColumnLabel(int column) {
        try {
            return meta.getColumnLabel(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getColumnName(int column) {
        try {
            return meta.getColumnName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getSchemaName(int column) {
        try {
            return meta.getSchemaName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getPrecision(int column) {
        try {
            return meta.getPrecision(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getScale(int column) {
        try {
            return meta.getScale(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getTableName(int column) {
        try {
            return meta.getTableName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getCatalogName(int column) {
        try {
            return meta.getCatalogName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getColumnType(int column) {
        try {
            return meta.getColumnType(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getColumnTypeName(int column) {
        try {
            return meta.getColumnTypeName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isReadOnly(int column) {
        try {
            return meta.isReadOnly(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWritable(int column) {
        try {
            return meta.isWritable(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isDefinitelyWritable(int column) {
        try {
            return meta.isDefinitelyWritable(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getColumnClassName(int column) {
        try {
            return meta.getColumnClassName(column);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

}
