package org.navitrace.model;


import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

@StorageName("tc_permitviolations")
public class Permitviolation extends BaseModel {

    private String violations;
    private double penalty;
    private long createdBy;
    private String createrName;

    public String getViolations() {
        return violations;
    }

    public void setViolations(String violations) {
        this.violations = violations;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

//    @QueryIgnore
//    public String getCreaterName() {
//        if(createdBy != 0 && Context.getUsersManager().getById(createdBy) != null)
//            return Context.getUsersManager().getById(createdBy).getName();
//        return null;
//    }
}