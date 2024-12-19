package org.navitrace.model;

import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;
import java.sql.SQLException;
import java.util.HashMap;

@StorageName("tc_reportedviolations")
public class Reportedviolation extends BaseModel {

    private String vehicleNumber;
    private long violationId;
    private String remark;
    private String status;
    private String image;
    private long reportedBy;
    private String violations;
    private String reporterName;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public long getViolationId() {
        return violationId;
    }

    public void setViolationId(long violationId) {
        this.violationId = violationId;
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

    public long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(long reportedBy) {
        this.reportedBy = reportedBy;
    }

//    @QueryIgnore
//    public String getViolations() {
//            try {
//                Permitviolation pViolation = Context.getDataManager().getObject(Permitviolation.class, violationId);
//                if(pViolation != null) {
//                    violations = pViolation.getViolations();
//                }else{
//                    violations = "Entery Not Found";
//                }
//            }catch(SQLException e) {
//                violations = "Entery Not Found";
//            }
//        return violations;
//    }
//
//    @QueryIgnore
//    public String getReporterName() {
//        if(reportedBy != 0 && Context.getUsersManager().getById(reportedBy) != null)
//            return Context.getUsersManager().getById(reportedBy).getName();
//        return null;
//    }

    private Date createdAt;

    public Date getCreatedAt() {
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
