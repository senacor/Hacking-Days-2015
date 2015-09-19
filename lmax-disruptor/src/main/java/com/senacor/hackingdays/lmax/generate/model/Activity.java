package com.senacor.hackingdays.lmax.generate.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {

    private static final long serialVersionUID = 1;

    private final Date lastLogin;
    private final int loginCount;

    public Activity(
            @JsonProperty("lastLogin") Date lastLogin,
            @JsonProperty("loginCount") int loginCount) {
        this.lastLogin = lastLogin;
        this.loginCount = loginCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public int getLoginCount() {
        return loginCount;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "lastLogin=" + lastLogin +
                ", loginCount=" + loginCount +
                '}';
    }

}
