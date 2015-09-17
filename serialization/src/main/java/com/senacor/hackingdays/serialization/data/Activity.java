package com.senacor.hackingdays.serialization.data;

import java.io.Serializable;
import java.time.Instant;

public class Activity implements Serializable{

    private static final long serialVersionUID = 1;

    private final Instant lastLogin;
    private final int loginFrequency;

    public Activity(Instant lastLogin, int loginFrequency) {
        this.lastLogin = lastLogin;
        this.loginFrequency = loginFrequency;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public int getLoginFrequency() {
        return loginFrequency;
    }
}
