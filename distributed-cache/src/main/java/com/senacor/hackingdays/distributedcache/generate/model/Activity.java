package com.senacor.hackingdays.distributedcache.generate.model;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {

    private static final long serialVersionUID = 1;

    private final Date lastLogin;
    private final int loginCount;

    public Activity(
            Date lastLogin,
            int loginCount) {
        this.lastLogin = lastLogin;
        this.loginCount = loginCount;
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
