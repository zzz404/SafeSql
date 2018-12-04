package zzz404.safesql.sql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import zzz404.safesql.util.CommonUtils;

public class QuietConnection implements Connection {

    protected Connection conn;

    public QuietConnection(Connection conn) {
        this.conn = conn;
    }

    public void close() {
        try {
            conn.close();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Statement createStatement() {
        try {
            return conn.createStatement();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) {
        try {
            return conn.createStatement(resultSetType, resultSetConcurrency);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            return conn.prepareStatement(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        try {
            return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T unwrap(Class<T> iface) {
        try {
            return conn.unwrap(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWrapperFor(Class<?> iface) {
        try {
            return conn.isWrapperFor(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public CallableStatement prepareCall(String sql) {
        try {
            return conn.prepareCall(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String nativeSQL(String sql) {
        try {
            return conn.nativeSQL(sql);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            conn.setAutoCommit(autoCommit);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getAutoCommit() {
        try {
            return conn.getAutoCommit();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void commit() {
        try {
            conn.commit();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void rollback() {
        try {
            conn.rollback();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isClosed() {
        try {
            return conn.isClosed();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public DatabaseMetaData getMetaData() {
        try {
            return conn.getMetaData();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setReadOnly(boolean readOnly) {
        try {
            conn.setReadOnly(readOnly);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isReadOnly() {
        try {
            return conn.isReadOnly();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setCatalog(String catalog) {
        try {
            conn.setCatalog(catalog);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getCatalog() {
        try {
            return conn.getCatalog();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTransactionIsolation(int level) {
        try {
            conn.setTransactionIsolation(level);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getTransactionIsolation() {
        try {
            return conn.getTransactionIsolation();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLWarning getWarnings() {
        try {
            return conn.getWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearWarnings() {
        try {
            conn.clearWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        try {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Map<String, Class<?>> getTypeMap() {
        try {
            return conn.getTypeMap();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setTypeMap(Map<String, Class<?>> map) {
        try {
            conn.setTypeMap(map);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setHoldability(int holdability) {
        try {
            conn.setHoldability(holdability);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getHoldability() {
        try {
            return conn.getHoldability();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Savepoint setSavepoint() {
        try {
            return conn.setSavepoint();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Savepoint setSavepoint(String name) {
        try {
            return conn.setSavepoint(name);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void rollback(Savepoint savepoint) {
        try {
            conn.rollback(savepoint);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void releaseSavepoint(Savepoint savepoint) {
        try {
            conn.releaseSavepoint(savepoint);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        try {
            return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        try {
            return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        try {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
        try {
            return conn.prepareStatement(sql, autoGeneratedKeys);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        try {
            return conn.prepareStatement(sql, columnIndexes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        try {
            return conn.prepareStatement(sql, columnNames);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Clob createClob() {
        try {
            return conn.createClob();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Blob createBlob() {
        try {
            return conn.createBlob();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public NClob createNClob() {
        try {
            return conn.createNClob();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLXML createSQLXML() {
        try {
            return conn.createSQLXML();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isValid(int timeout) {
        try {
            return conn.isValid(timeout);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            conn.setClientInfo(name, value);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            conn.setClientInfo(properties);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getClientInfo(String name) {
        try {
            return conn.getClientInfo(name);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Properties getClientInfo() {
        try {
            return conn.getClientInfo();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Array createArrayOf(String typeName, Object[] elements) {
        try {
            return conn.createArrayOf(typeName, elements);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Struct createStruct(String typeName, Object[] attributes) {
        try {
            return conn.createStruct(typeName, attributes);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setSchema(String schema) {
        try {
            conn.setSchema(schema);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getSchema() {
        try {
            return conn.getSchema();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void abort(Executor executor) {
        try {
            conn.abort(executor);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) {
        try {
            conn.setNetworkTimeout(executor, milliseconds);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getNetworkTimeout() {
        try {
            return conn.getNetworkTimeout();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

}
