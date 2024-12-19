
package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_groups")
public class Group extends GroupedModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
