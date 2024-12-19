package org.navitrace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_tokens")
public class Token  extends BaseModel{
    public static final long TIMEOUT_INTERVAL = 31 * 24*3600*1000l ;
    private long userId ;
    private String userToken ;
    private boolean isLogin ;
    private boolean isThirdParty ;
    private Date createdOn ;
    private  boolean disabled ;
    private Date lastSeenAt;
    private int fialedCounter;

    @JsonIgnore
    @QueryIgnore
    public int getFialedCounter() {
        return fialedCounter;
    }

    @QueryIgnore
    public void setFialedCounter(int fialedCounter) {
        this.fialedCounter = fialedCounter;
    }

    @JsonIgnore
    @QueryIgnore
    public Date getLastSeenAt() {
        return lastSeenAt ;
    }

    @QueryIgnore
    public void setLastSeenAt(Date lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }

    public boolean getIsThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(boolean thirdParty) {
        isThirdParty = thirdParty;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
