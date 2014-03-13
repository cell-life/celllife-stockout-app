package org.celllife.stockout.app.domain;

import java.io.Serializable;

/**
 * Domain object that describes the Stock history for a specific Drug.
 */
public class StockHistory implements Serializable {

	private static final long serialVersionUID = 6871132613107093258L;

	private Long id;
    
    private Drug drug;
    
    private Integer averageDailyConsumption;

    public StockHistory() {
    	
    }

	public StockHistory(Drug drug, Integer averageDailyConsumption) {
		super();
		this.drug = drug;
		this.averageDailyConsumption = averageDailyConsumption;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public Integer getAverageDailyConsumption() {
		return averageDailyConsumption;
	}

	public void setAverageDailyConsumption(Integer averageDailyConsumption) {
		this.averageDailyConsumption = averageDailyConsumption;
	}

	@Override
	public String toString() {
		return "StockHistory [id=" + id + ", drug=" + drug + ", averageDailyConsumption=" + averageDailyConsumption
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((averageDailyConsumption == null) ? 0 : averageDailyConsumption.hashCode());
		result = prime * result + ((drug == null) ? 0 : drug.hashCode());
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
		StockHistory other = (StockHistory) obj;
		if (averageDailyConsumption == null) {
			if (other.averageDailyConsumption != null)
				return false;
		} else if (!averageDailyConsumption.equals(other.averageDailyConsumption))
			return false;
		if (drug == null) {
			if (other.drug != null)
				return false;
		} else if (!drug.equals(other.drug))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
