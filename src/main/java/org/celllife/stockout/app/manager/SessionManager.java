package org.celllife.stockout.app.manager;

import java.util.Date;

import org.celllife.stockout.app.ui.activities.MainActivity;


/**
 * Manages the user's session and is able to determine when their session has expired
 */
public interface SessionManager {
	
	/**
	 * Indicates that the user has just performed an action. This keeps their session alive
	 */
	void markInteraction();
	
	/**
	 * Marks the user as authenticated. Call this method after a successful authentication
	 * @param username String msisdn
	 * @param password String clear text pin
	 */
	void authenticated(String username, String password);

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

	/**
	 * Retrieves the username associated with the current session
	 * @return String msisdn
	 */
	String getUsername();

	/**
	 * Retrieves the clear text password associated with the current session
	 * @return String clear text pin
	 */
	String getPassword();
	
	/**
	 * Sets a reference to the current MainActivity so that we are able to refresh the screen at a
	 * later date. Should be set during onCreate method of MainActivity
	 * 
	 * @param mainActivity MainActivity currently displayed to the user
	 */
	void setMainActivity(MainActivity mainActivity);

	/**
	 * Retrieve the current MainActivity being displayed to the user.
	 * 
	 * @return MainActivity, can be null
	 */
	MainActivity getMainActivity();
}