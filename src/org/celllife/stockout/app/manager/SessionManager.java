package org.celllife.stockout.app.manager;

import java.util.Date;


/**
 * Manages the user's session and is able to determine when their session has expired
 */
public interface SessionManager {
	
	/**
	 * Indicates that the user has just performed an action. This keeps their session alive
	 */
	void markInteraction();
	
	/**
	 * Marks the user as authenticated. Call this methd
	 */
	void authenticated();

	/**
	 * Indicates if the user's session has ended (or if it hasn't started yet).
	 * This method is used to determine if the user should be prompted for their password.
	 * @return boolean true if their session has expired
	 */
	boolean isSessionExpired();

	/**
	 * Retrieves the timestamp of the user's last interaction
	 * @return Date/time of the last interaction, or null if they don't have a session
	 */
	Date getLastInteractionDate();

	/**
	 * Retrieves the timestamp of the start of the user's session
	 * @return Date/time of the start of the user's session, or null if they don't have a session
	 */
	Date getSessionStartDate();
}