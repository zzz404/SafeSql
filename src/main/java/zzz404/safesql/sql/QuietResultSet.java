package zzz404.safesql.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import zzz404.safesql.util.CommonUtils;

public class QuietResultSet implements ResultSet {
    private ResultSet rs = null;

    public QuietResultSet(ResultSet rs) {
        this.rs = rs;
    }

    public QuietResultSetMetaData getMetaData() {
        try {
            return new QuietResultSetMetaData(rs.getMetaData());
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T unwrap(Class<T> iface) {
        try {
            return rs.unwrap(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isWrapperFor(Class<?> iface) {
        try {
            return rs.isWrapperFor(iface);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean next() {
        try {
            return rs.next();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void close() {
        try {
            rs.close();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean wasNull() {
        try {
            return rs.wasNull();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getString(int columnIndex) {
        try {
            return rs.getString(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getBoolean(int columnIndex) {
        try {
            return rs.getBoolean(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public byte getByte(int columnIndex) {
        try {
            return rs.getByte(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public short getShort(int columnIndex) {
        try {
            return rs.getShort(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getInt(int columnIndex) {
        try {
            return rs.getInt(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLong(int columnIndex) {
        try {
            return rs.getLong(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public float getFloat(int columnIndex) {
        try {
            return rs.getFloat(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public double getDouble(int columnIndex) {
        try {
            return rs.getDouble(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(int columnIndex, int scale) {
        try {
            return rs.getBigDecimal(columnIndex, scale);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public byte[] getBytes(int columnIndex) {
        try {
            return rs.getBytes(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Date getDate(int columnIndex) {
        try {
            return rs.getDate(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Time getTime(int columnIndex) {
        try {
            return rs.getTime(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Timestamp getTimestamp(int columnIndex) {
        try {
            return rs.getTimestamp(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public InputStream getAsciiStream(int columnIndex) {
        try {
            return rs.getAsciiStream(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(int columnIndex) {
        try {
            return rs.getUnicodeStream(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public InputStream getBinaryStream(int columnIndex) {
        try {
            return rs.getBinaryStream(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getString(String columnLabel) {
        try {
            return rs.getString(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean getBoolean(String columnLabel) {
        try {
            return rs.getBoolean(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public byte getByte(String columnLabel) {
        try {
            return rs.getByte(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public short getShort(String columnLabel) {
        try {
            return rs.getShort(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getInt(String columnLabel) {
        try {
            return rs.getInt(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public long getLong(String columnLabel) {
        try {
            return rs.getLong(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public float getFloat(String columnLabel) {
        try {
            return rs.getFloat(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public double getDouble(String columnLabel) {
        try {
            return rs.getDouble(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(String columnLabel, int scale) {
        try {
            return rs.getBigDecimal(columnLabel, scale);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public byte[] getBytes(String columnLabel) {
        try {
            return rs.getBytes(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Date getDate(String columnLabel) {
        try {
            return rs.getDate(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Time getTime(String columnLabel) {
        try {
            return rs.getTime(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Timestamp getTimestamp(String columnLabel) {
        try {
            return rs.getTimestamp(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public InputStream getAsciiStream(String columnLabel) {
        try {
            return rs.getAsciiStream(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(String columnLabel) {
        try {
            return rs.getUnicodeStream(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public InputStream getBinaryStream(String columnLabel) {
        try {
            return rs.getBinaryStream(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLWarning getWarnings() {
        try {
            return rs.getWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void clearWarnings() {
        try {
            rs.clearWarnings();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getCursorName() {
        try {
            return rs.getCursorName();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Object getObject(int columnIndex) {
        try {
            return rs.getObject(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Object getObject(String columnLabel) {
        try {
            return rs.getObject(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int findColumn(String columnLabel) {
        try {
            return rs.findColumn(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Reader getCharacterStream(int columnIndex) {
        try {
            return rs.getCharacterStream(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Reader getCharacterStream(String columnLabel) {
        try {
            return rs.getCharacterStream(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return rs.getBigDecimal(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public BigDecimal getBigDecimal(String columnLabel) {
        try {
            return rs.getBigDecimal(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isBeforeFirst() {
        try {
            return rs.isBeforeFirst();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isAfterLast() {
        try {
            return rs.isAfterLast();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isFirst() {
        try {
            return rs.isFirst();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isLast() {
        try {
            return rs.isLast();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void beforeFirst() {
        try {
            rs.beforeFirst();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void afterLast() {
        try {
            rs.afterLast();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean first() {
        try {
            return rs.first();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean last() {
        try {
            return rs.last();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getRow() {
        try {
            return rs.getRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean absolute(int row) {
        try {
            return rs.absolute(row);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean relative(int rows) {
        try {
            return rs.relative(rows);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean previous() {
        try {
            return rs.previous();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchDirection(int direction) {
        try {
            rs.setFetchDirection(direction);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchDirection() {
        try {
            return rs.getFetchDirection();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void setFetchSize(int rows) {
        try {
            rs.setFetchSize(rows);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getFetchSize() {
        try {
            return rs.getFetchSize();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getType() {
        try {
            return rs.getType();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getConcurrency() {
        try {
            return rs.getConcurrency();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean rowUpdated() {
        try {
            return rs.rowUpdated();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean rowInserted() {
        try {
            return rs.rowInserted();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean rowDeleted() {
        try {
            return rs.rowDeleted();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNull(int columnIndex) {
        try {
            rs.updateNull(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBoolean(int columnIndex, boolean x) {
        try {
            rs.updateBoolean(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateByte(int columnIndex, byte x) {
        try {
            rs.updateByte(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateShort(int columnIndex, short x) {
        try {
            rs.updateShort(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateInt(int columnIndex, int x) {
        try {
            rs.updateInt(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateLong(int columnIndex, long x) {
        try {
            rs.updateLong(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateFloat(int columnIndex, float x) {
        try {
            rs.updateFloat(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateDouble(int columnIndex, double x) {
        try {
            rs.updateDouble(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) {
        try {
            rs.updateBigDecimal(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateString(int columnIndex, String x) {
        try {
            rs.updateString(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBytes(int columnIndex, byte[] x) {
        try {
            rs.updateBytes(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateDate(int columnIndex, Date x) {
        try {
            rs.updateDate(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateTime(int columnIndex, Time x) {
        try {
            rs.updateTime(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateTimestamp(int columnIndex, Timestamp x) {
        try {
            rs.updateTimestamp(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) {
        try {
            rs.updateAsciiStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) {
        try {
            rs.updateBinaryStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) {
        try {
            rs.updateCharacterStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(int columnIndex, Object x, int scaleOrLength) {
        try {
            rs.updateObject(columnIndex, x, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(int columnIndex, Object x) {
        try {
            rs.updateObject(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNull(String columnLabel) {
        try {
            rs.updateNull(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBoolean(String columnLabel, boolean x) {
        try {
            rs.updateBoolean(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateByte(String columnLabel, byte x) {
        try {
            rs.updateByte(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateShort(String columnLabel, short x) {
        try {
            rs.updateShort(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateInt(String columnLabel, int x) {
        try {
            rs.updateInt(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateLong(String columnLabel, long x) {
        try {
            rs.updateLong(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateFloat(String columnLabel, float x) {
        try {
            rs.updateFloat(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateDouble(String columnLabel, double x) {
        try {
            rs.updateDouble(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBigDecimal(String columnLabel, BigDecimal x) {
        try {
            rs.updateBigDecimal(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateString(String columnLabel, String x) {
        try {
            rs.updateString(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBytes(String columnLabel, byte[] x) {
        try {
            rs.updateBytes(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateDate(String columnLabel, Date x) {
        try {
            rs.updateDate(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateTime(String columnLabel, Time x) {
        try {
            rs.updateTime(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateTimestamp(String columnLabel, Timestamp x) {
        try {
            rs.updateTimestamp(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x, int length) {
        try {
            rs.updateAsciiStream(columnLabel, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x, int length) {
        try {
            rs.updateBinaryStream(columnLabel, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader, int length) {
        try {
            rs.updateCharacterStream(columnLabel, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(String columnLabel, Object x, int scaleOrLength) {
        try {
            rs.updateObject(columnLabel, x, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(String columnLabel, Object x) {
        try {
            rs.updateObject(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void insertRow() {
        try {
            rs.insertRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateRow() {
        try {
            rs.updateRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void deleteRow() {
        try {
            rs.deleteRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void refreshRow() {
        try {
            rs.refreshRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void cancelRowUpdates() {
        try {
            rs.cancelRowUpdates();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void moveToInsertRow() {
        try {
            rs.moveToInsertRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void moveToCurrentRow() {
        try {
            rs.moveToCurrentRow();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Statement getStatement() {
        try {
            return rs.getStatement();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        try {
            return rs.getObject(columnIndex, map);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Ref getRef(int columnIndex) {
        try {
            return rs.getRef(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Blob getBlob(int columnIndex) {
        try {
            return rs.getBlob(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Clob getClob(int columnIndex) {
        try {
            return rs.getClob(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Array getArray(int columnIndex) {
        try {
            return rs.getArray(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) {
        try {
            return rs.getObject(columnLabel, map);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Ref getRef(String columnLabel) {
        try {
            return rs.getRef(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Blob getBlob(String columnLabel) {
        try {
            return rs.getBlob(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Clob getClob(String columnLabel) {
        try {
            return rs.getClob(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Array getArray(String columnLabel) {
        try {
            return rs.getArray(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Date getDate(int columnIndex, Calendar cal) {
        try {
            return rs.getDate(columnIndex, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Date getDate(String columnLabel, Calendar cal) {
        try {
            return rs.getDate(columnLabel, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Time getTime(int columnIndex, Calendar cal) {
        try {
            return rs.getTime(columnIndex, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Time getTime(String columnLabel, Calendar cal) {
        try {
            return rs.getTime(columnLabel, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) {
        try {
            return rs.getTimestamp(columnIndex, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal) {
        try {
            return rs.getTimestamp(columnLabel, cal);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public URL getURL(int columnIndex) {
        try {
            return rs.getURL(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public URL getURL(String columnLabel) {
        try {
            return rs.getURL(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateRef(int columnIndex, Ref x) {
        try {
            rs.updateRef(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateRef(String columnLabel, Ref x) {
        try {
            rs.updateRef(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(int columnIndex, Blob x) {
        try {
            rs.updateBlob(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(String columnLabel, Blob x) {
        try {
            rs.updateBlob(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(int columnIndex, Clob x) {
        try {
            rs.updateClob(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(String columnLabel, Clob x) {
        try {
            rs.updateClob(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateArray(int columnIndex, Array x) {
        try {
            rs.updateArray(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateArray(String columnLabel, Array x) {
        try {
            rs.updateArray(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public RowId getRowId(int columnIndex) {
        try {
            return rs.getRowId(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public RowId getRowId(String columnLabel) {
        try {
            return rs.getRowId(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateRowId(int columnIndex, RowId x) {
        try {
            rs.updateRowId(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateRowId(String columnLabel, RowId x) {
        try {
            rs.updateRowId(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public int getHoldability() {
        try {
            return rs.getHoldability();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public boolean isClosed() {
        try {
            return rs.isClosed();
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNString(int columnIndex, String nString) {
        try {
            rs.updateNString(columnIndex, nString);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNString(String columnLabel, String nString) {
        try {
            rs.updateNString(columnLabel, nString);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(int columnIndex, NClob nClob) {
        try {
            rs.updateNClob(columnIndex, nClob);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(String columnLabel, NClob nClob) {
        try {
            rs.updateNClob(columnLabel, nClob);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public NClob getNClob(int columnIndex) {
        try {
            return rs.getNClob(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public NClob getNClob(String columnLabel) {
        try {
            return rs.getNClob(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLXML getSQLXML(int columnIndex) {
        try {
            return rs.getSQLXML(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public SQLXML getSQLXML(String columnLabel) {
        try {
            return rs.getSQLXML(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) {
        try {
            rs.updateSQLXML(columnIndex, xmlObject);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) {
        try {
            rs.updateSQLXML(columnLabel, xmlObject);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getNString(int columnIndex) {
        try {
            return rs.getNString(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public String getNString(String columnLabel) {
        try {
            return rs.getNString(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Reader getNCharacterStream(int columnIndex) {
        try {
            return rs.getNCharacterStream(columnIndex);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public Reader getNCharacterStream(String columnLabel) {
        try {
            return rs.getNCharacterStream(columnLabel);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) {
        try {
            rs.updateNCharacterStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
        try {
            rs.updateNCharacterStream(columnLabel, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) {
        try {
            rs.updateAsciiStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) {
        try {
            rs.updateBinaryStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) {
        try {
            rs.updateCharacterStream(columnIndex, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length) {
        try {
            rs.updateAsciiStream(columnLabel, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length) {
        try {
            rs.updateBinaryStream(columnLabel, x, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length) {
        try {
            rs.updateCharacterStream(columnLabel, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) {
        try {
            rs.updateBlob(columnIndex, inputStream, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length) {
        try {
            rs.updateBlob(columnLabel, inputStream, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(int columnIndex, Reader reader, long length) {
        try {
            rs.updateClob(columnIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(String columnLabel, Reader reader, long length) {
        try {
            rs.updateClob(columnLabel, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(int columnIndex, Reader reader, long length) {
        try {
            rs.updateNClob(columnIndex, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(String columnLabel, Reader reader, long length) {
        try {
            rs.updateNClob(columnLabel, reader, length);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNCharacterStream(int columnIndex, Reader x) {
        try {
            rs.updateNCharacterStream(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) {
        try {
            rs.updateNCharacterStream(columnLabel, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(int columnIndex, InputStream x) {
        try {
            rs.updateAsciiStream(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(int columnIndex, InputStream x) {
        try {
            rs.updateBinaryStream(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(int columnIndex, Reader x) {
        try {
            rs.updateCharacterStream(columnIndex, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateAsciiStream(String columnLabel, InputStream x) {
        try {
            rs.updateAsciiStream(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBinaryStream(String columnLabel, InputStream x) {
        try {
            rs.updateBinaryStream(columnLabel, x);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateCharacterStream(String columnLabel, Reader reader) {
        try {
            rs.updateCharacterStream(columnLabel, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(int columnIndex, InputStream inputStream) {
        try {
            rs.updateBlob(columnIndex, inputStream);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateBlob(String columnLabel, InputStream inputStream) {
        try {
            rs.updateBlob(columnLabel, inputStream);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(int columnIndex, Reader reader) {
        try {
            rs.updateClob(columnIndex, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateClob(String columnLabel, Reader reader) {
        try {
            rs.updateClob(columnLabel, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(int columnIndex, Reader reader) {
        try {
            rs.updateNClob(columnIndex, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateNClob(String columnLabel, Reader reader) {
        try {
            rs.updateNClob(columnLabel, reader);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T getObject(int columnIndex, Class<T> type) {
        try {
            return rs.getObject(columnIndex, type);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public <T> T getObject(String columnLabel, Class<T> type) {
        try {
            return rs.getObject(columnLabel, type);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) {
        try {
            rs.updateObject(columnIndex, x, targetSqlType, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) {
        try {
            rs.updateObject(columnLabel, x, targetSqlType, scaleOrLength);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) {
        try {
            rs.updateObject(columnIndex, x, targetSqlType);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) {
        try {
            rs.updateObject(columnLabel, x, targetSqlType);
        }
        catch (Exception e) {
            throw CommonUtils.wrapToRuntime(e);
        }
    }
}
