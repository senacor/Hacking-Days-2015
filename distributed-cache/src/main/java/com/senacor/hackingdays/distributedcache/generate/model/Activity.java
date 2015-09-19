package com.senacor.hackingdays.distributedcache.generate.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.common.base.Objects;

public class Activity implements Serializable {

	private static final long serialVersionUID = 1;

	private final Date lastLogin;
	private final int loginCount;

	public Activity(Date lastLogin, int loginCount) {
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
		return "Activity{" + "lastLogin=" + lastLogin + ", loginCount=" + loginCount + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Activity))
			return false;

		Activity rhs = (Activity) obj;

		return new EqualsBuilder() //
				.append(this.loginCount, rhs.loginCount) //
				.append(this.lastLogin, rhs.lastLogin) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.loginCount, this.lastLogin);
	}

}
