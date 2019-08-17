package zzz404.safesql.sync;

import zzz404.safesql.sql.proxy.QuietResultSet;

public class Organ {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Organ valueOf(QuietResultSet rs) {
        Organ org = new Organ();
        org.code = rs.getString("code");
        org.name = rs.getString("name");
        return org;
    }

    public void updateNameTo(QuietResultSet rs) {
        rs.updateString("name", name);
    }

    public void updateTo(QuietResultSet rs) {
        updateNameTo(rs);
    }

}
