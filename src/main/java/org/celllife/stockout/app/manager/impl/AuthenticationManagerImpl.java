package org.celllife.stockout.app.manager.impl;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.GetUserMethod;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.celllife.stockout.app.manager.AuthenticationManager;
import org.celllife.stockout.app.manager.DatabaseManager;

import android.util.Log;

public class AuthenticationManagerImpl implements AuthenticationManager {

	@Override
	public boolean authenticate(String username, String password) {
		// get the user cached in the app database
		Phone currentUser = getPhone();
		Log.d("AuthenticationManager", "AM: found cache currentUser="+currentUser);
		if (currentUser == null || currentUser.getPassword() == null) {
			// retrieve the user details from the server (and cache the details in the database)
			try {
				currentUser = getUserDetailsFromServer(currentUser, username, password);
				// Note: at this point the RestCommunicationException could be encountered. 
				// In this case we wish to display this error to the user so they can
				// debug the problem with support
			} catch (RestAuthenticationException e) {
				// However, if they weren't authenticated successfully while trying to read the user, 
				// the supplied credentials are obviously wrong
				return false;
			}
			Log.d("AuthenticationManager", "AM: found server currentUser="+currentUser);
		}
		if (currentUser.getPassword() != null) {
			// check if the encrypted passwords match
			boolean isValid = isValidUserPassword(currentUser, password);
			if (!isValid) {
				// if they don't match, then perhaps they have changed their password on the server ...
				try {
					currentUser = getUserDetailsFromServer(currentUser, username, password);
				} catch (RestAuthenticationException e) {
					// if they weren't authenticated successfully while trying to read the user, 
					// the supplied credentials are obviously wrong
					return false;
				} catch (RestCommunicationException e) {
					// if we get a communication error, then just carry on with the previous result 
					// (maybe the server is down?)
				}
			}
			Log.d("AuthenticationManager", "AM: isValid="+isValid);
			return isValid;
		} else {
			// if we still don't know the password, just deny access
			// this shouldn't really happen due handling of communication exceptions above.
			// it could be that the hash + salt are null on the server (?)
			Log.d("AuthenticationManager", "AM: invalid password");
			return false;
		}
	}
	
	private Phone getUserDetailsFromServer(Phone existingDetails, String username, String password) 
			throws RestCommunicationException {
		Phone userDetails = existingDetails;
		if (userDetails == null) {
			userDetails = new Phone();
			userDetails.setMsisdn(username);
		}
		Phone newUserDetails = GetUserMethod.getUserDetails(username, password);
		// merge details
		if (newUserDetails != null) {
			userDetails.setPassword(newUserDetails.getPassword());
			userDetails.setClinicCode(newUserDetails.getClinicCode());
			userDetails.setClinicName(newUserDetails.getClinicName());
			DatabaseManager.getPhoneTableAdapter().insertOrUpdate(userDetails);
		} else {
			throw new RestCommunicationException("Error while reading the response from the server. " +
					"Error: did not get any response");
		}
		return userDetails;
	}

	private Boolean isValidUserPassword(Phone user, String password) {
	    return (user.getPassword().equals(password));
	}

	@Override
	public Phone getPhone() {
		PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
		Phone phone = phoneDb.findOne();
		return phone;
	}
}
