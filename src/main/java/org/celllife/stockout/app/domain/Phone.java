package org.celllife.stockout.app.domain;

import org.celllife.stockout.app.database.framework.Entity;

/**
 * Domain object to represent the Phone in the system - have a unique msisdn and belong to a clinic
 */
public class Phone implements Entity {

	private static final long serialVersionUID = 1963884212116602873L;

    private Long id;
	
	private String msisdn;
	
	private String password;
	
	private String clinicCode;
	
	private String clinicName;

	private Integer drugLeadTime;
	
	private Integer drugSafetyLevel;

	private Boolean activated;
	
	public Phone() {
	    this.activated = Boolean.FALSE;
	}

   public Phone(String msisdn, String password) {
        super();
        this.msisdn = msisdn;
        this.password = password;
        this.activated = Boolean.FALSE;
    }

	public Phone(String msisdn, String password, String clinicCode, String clinicName) {
		super();
		this.msisdn = msisdn;
		this.password = password;
		this.clinicCode = clinicCode;
		this.clinicName = clinicName;
		this.activated = Boolean.FALSE;
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

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

	public Integer getDrugLeadTime() {
		return drugLeadTime;
	}

	public void setDrugLeadTime(Integer drugLeadTime) {
		this.drugLeadTime = drugLeadTime;
	}

	public Integer getDrugSafetyLevel() {
		return drugSafetyLevel;
	}

	public void setDrugSafetyLevel(Integer drugSafetyLevel) {
		this.drugSafetyLevel = drugSafetyLevel;
	}

    public Boolean isActivated() {
        return activated;
    }

	public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
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
		Phone other = (Phone) obj;
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
        return "Phone [id=" + id + ", msisdn=" + msisdn + ", password=" + password + ", clinicCode=" + clinicCode
                + ", clinicName=" + clinicName + ", drugLeadTime=" + drugLeadTime + ", drugSafetyLevel="
                + drugSafetyLevel + ", activated=" + activated + "]";
    }

}
