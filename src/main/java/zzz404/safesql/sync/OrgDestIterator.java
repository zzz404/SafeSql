package zzz404.safesql.sync;

import zzz404.safesql.sql.proxy.QuietResultSet;

public class OrgDestIterator implements DestIterator<Organ> {

    private Organ currentOrgan;
    private QuietResultSet rs;

    public OrgDestIterator(QuietResultSet rs) {
        this.rs = rs;
    }

    @Override
    public Organ next() {
        if (!rs.next()) {
            currentOrgan = null;
        }
        else {
            currentOrgan = Organ.valueOf(rs);
        }
        return currentOrgan;
    }

    @Override
    public void insert(Organ sourceOrg) {
        rs.moveToInsertRow();
        sourceOrg.updateTo(rs);
        rs.insertRow();
        rs.moveToCurrentRow();
    }

    @Override
    public void update(Organ sourceOrg) {
        boolean updated = false;
        if (!sourceOrg.getName().equals(currentOrgan.getName())) {
            sourceOrg.updateNameTo(rs);
            updated = true;
        }
        if (updated) {
            rs.updateRow();
        }
    }

    @Override
    public void remove() {
        rs.deleteRow();
    }

}
