package org.celllife.stockout.app.domain;

import java.io.Serializable;

/**
 * Domain object to represent the User in the system - have a unique msisdn and belong to a clinic
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1963884212116602873L;

    private Long id;
	
	private String msisdn;
	
	private String encryptedPassword;
	
	private String salt;
	
	private String clinicCode;
	
	private String clinicName;
	
	public User() {
		
	}

	public User(String msisdn, String encryptedPassword, String salt, String clinicCode, String clinicName) {
		super();
		this.msisdn = msisdn;
		this.encryptedPassword = encryptedPassword;
		this.salt = salt;
		this.clinicCode = clinicCode;
		this.clinicName = clinicName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getClinicCode() {
		return clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (msisdn == null) {
			if (other.msisdn != null)
				return false;
		} else if (!msisdn.equals(other.msisdn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", msisdn=" + msisdn + ", encryptedPassword=" + encryptedPassword + ", salt=" + salt
				+ ", clinicCode=" + clinicCode + ", clinicName=" + clinicName + "]";
	}
}
