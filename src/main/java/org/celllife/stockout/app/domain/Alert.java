package org.celllife.stockout.app.domain;

import java.util.Date;

import org.celllife.stockout.app.database.framework.Entity;

/**
 * Domain object to represent Alerts in the system - they are for a specific
 * Drug. Alerts have a level indicating importance and a status {@see
 * AlertStatus} to indicate the lifecycle of the Alert
 */
public class Alert implements Entity {

	private static final long serialVersionUID = -6226563319400467361L;

	private Long id;

	private Date date;

	private Integer level;

	private String message;

	private AlertStatus status;

	private Drug drug;

	public Alert() {

	}

	public Alert(Date date, Integer level, String message, AlertStatus status, Drug drug) {
		super();
		this.date = date;
		this.level = level;
		this.message = message;
		this.status = status;
		this.drug = drug;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", date=" + date + ", level=" + level + ", message=" + message + ", status="
				+ status + ", drug=" + drug + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Alert other = (Alert) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}