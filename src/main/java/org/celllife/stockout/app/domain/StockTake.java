package org.celllife.stockout.app.domain;

import java.util.Date;

import org.celllife.stockout.app.database.framework.Entity;

/**
 * Domain object which describes the amount of stock counted for a particular drug at a particular time.
 */
public class StockTake implements Entity {

	private static final long serialVersionUID = 4497991414545347704L;

	private Long id;

    private Date date;

    private Drug drug;

    private Integer quantity;
    
    /** Indicates if the stock take was successfully sent to the StockOut App Server*/
    private Boolean submitted;

    public StockTake() {
    	
    }

	public StockTake(Date date, Drug drug, Integer quantity, Boolean submitted) {
		super();
		this.date = date;
		this.drug = drug;
		this.quantity = quantity;
		this.submitted = submitted;
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

	public Drug getDrug() {
		return drug;
	}

	public void setDrug(Drug drug) {
		this.drug = drug;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getSubmitted() {
		return submitted;
	}

	public Boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(Boolean submitted) {
		this.submitted = submitted;
	}

	@Override
	public String toString() {
		return "StockTake [id=" + id + ", date=" + date + ", drug=" + drug + ", quantity=" + quantity + ", submitted="
				+ submitted + "]";
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
		StockTake other = (StockTake) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
