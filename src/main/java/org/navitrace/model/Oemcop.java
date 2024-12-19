package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_oemcops")
public class Oemcop extends BaseModel {
    private long oemId;
    private String copno;
    public long getOemId() {
        return oemId;
    }

    public void setOemId(long oemId) {
        this.oemId = oemId;
    }

    public String getCopno() {
        return copno;
    }

    public void setCopno(String copno) {
        this.copno = copno;
    }
}
