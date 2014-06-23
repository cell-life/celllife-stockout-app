package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.domain.Phone;

/**
 * Service used to authenticate users
 */
public interface AuthenticationManager {

	/**
	 * Authenticates the user given the username and password. Will used cached user details
	 * whenever possible to avoid doing a server call.
	 * @param username String username (usually corresponds to the msisdn of the phone)
	 * @param password String clear text password (usually 4 digit pin number)
	 * @return boolean true if the user is authenticated, false if the credentials were invalid
	 * FIXME: what to do about communicate failure?
	 */
	boolean authenticate(String username, String password);

	/**
	 * Returns the phone entity stored in the phone db. The phone entity allows you to determine
	 * the currently installed msisdn (which is required for authentication)
	 */
	Phone getPhone();
}
