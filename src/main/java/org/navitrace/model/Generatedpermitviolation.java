package org.navitrace.model;

import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@StorageName("tc_generatedpermitviolations")
public class Generatedpermitviolation extends BaseModel{

    private String vehicleNumber;
    private LinkedList<Integer> violationIds = new LinkedList<>();
    private String remark;
    private String status;
    private String image;
    private double totalPenalty;
    private long reportedBy;
    private Map<Long, String> violations;

//    @QueryIgnore
//    public Map<Long, String> getViolations(){
//        violations = new HashMap<>();
//        for(long violationId : violationIds){
//            try {
//                Permitviolation pViolation = Context.getDataManager().getObject(Permitviolation.class, violationId);
//                if(pViolation!=null) {
//                    violations.put(violationId, pViolation.getViolations());
//                }else{
//                    violations.put(violationId, "Entery Not Found");
//                }
//            }catch(SQLException e) {
//                violations.put(violationId, "Entery Not Found");
//            }
//        }
//        return violations;
//    }

    private String reporterName;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LinkedList<Integer> getViolationIds() {
        return violationIds;
    }

    public void setViolationIds(LinkedList<Integer> violationIds) {
        this.violationIds = violationIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(double totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

    public long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(long reportedBy) {
        this.reportedBy = reportedBy;
    }

//    @QueryIgnore
//    public String getReporterName() {
//        if(reportedBy != 0 && Context.getUsersManager().getById(reportedBy) != null)
//            return Context.getUsersManager().getById(reportedBy).getName();
//        return null;
//    }

    private Date createdAt;

    public Date getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

//    @QueryIgnore
//    public String getPhone(){
//        if(reportedBy != 0 && Context.getUsersManager().getById(reportedBy) != null){
//            return Context.getUsersManager().getById(reportedBy).getPhone();
//        }
//        return null;
//    }
}
