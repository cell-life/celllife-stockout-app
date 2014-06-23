package org.celllife.stockout.app.database.framework;

import java.io.Serializable;

/**
 * Defines an Entity which can be persisted into the database
 */
public interface Entity extends Serializable {

	/**
	 * Retrieves the Entity identifier. Can be null or 0 if undefined
	 * @return
	 */
	Long getId();

	/**
	 * Sets the Entity's identifier once the Entity has been persisted/
	 * @param id
	 */
	void setId(Long id);
}
