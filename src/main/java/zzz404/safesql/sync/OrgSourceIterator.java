package zzz404.safesql.sync;

import zzz404.safesql.sql.proxy.QuietResultSet;

public class OrgSourceIterator implements SourceIterator<Organ> {
    private QuietResultSet rs;

    public OrgSourceIterator(QuietResultSet rs) {
        this.rs = rs;
    }

    @Override
    public Organ next() {
        if(!rs.next()) {
            return null;
        }
        return Organ.valueOf(rs);
    }
    
    
}
