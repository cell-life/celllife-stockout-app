package org.celllife.stockout.app.manager.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

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
		Phone currentUser = getCachedPhoneDetails();
		Log.d("AuthenticationManager", "AM: found cache currentUser="+currentUser);
		if (currentUser == null || currentUser.getEncryptedPassword() == null || currentUser.getSalt() == null) {
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
		if (currentUser.getEncryptedPassword() != null && currentUser.getSalt() != null) {
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
			// if we still don't know the encrypted password & salt at this stage, just deny access
			// this shouldn't really happen due handling of communication exceptions above.
			// it could be that the hash + salt are null on the server (?)
			Log.d("AuthenticationManager", "AM: invalid password");
			return false;
		}
	}
	
	private Phone getCachedPhoneDetails() {
		PhoneTableAdapter phoneDb = DatabaseManager.getPhoneTableAdapter();
		List<Phone> phones = phoneDb.findAll();
		// assumption is that there is only ever 1 phone created in the database.
		if (phones.size() > 0) {
			return phones.get(0);
		}
		return null;
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
			userDetails.setEncryptedPassword(newUserDetails.getEncryptedPassword());
			userDetails.setSalt(newUserDetails.getSalt());
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
		String encryptedPassword = user.getEncryptedPassword();
		String salt = user.getSalt();

		try {
			String hashedPassword = encodeString(password + salt);
			if (hashedPassword.equals(encryptedPassword)) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			Log.e("AuthenticationManager", "Could not encode the password. It will be marked as invalid. Error:"+e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			Log.e("AuthenticationManager", "Could not encode the password. It will be marked as invalid. Error:"+e.getMessage(), e);
		}
		return false;
	}

	// copied from the server: FIXME: look into a shared library before editing this
	private static String encodeString(String strToEncode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String algorithm = "SHA-256";
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] input = strToEncode.getBytes("UTF-8");
		return hexString(md.digest(input));	
	}

	// copied from the server. FIXME: look into a shared library before editing this
	public static String getRandomToken() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Random rng = new Random();
		return encodeString(Long.toString(System.currentTimeMillis()) + Long.toString(rng.nextLong()));
	}
	
	// copied from the server: FIXME: look into a shared library before editing this
	private static String hexString(byte[] b) {
		StringBuffer buf = new StringBuffer();
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		int len = b.length;
		int high = 0;
		int low = 0;
		for (int i = 0; i < len; i++) {
			high = ((b[i] & 0xf0) >> 4);
			low = (b[i] & 0x0f);
			buf.append(hexChars[high]);
			buf.append(hexChars[low]);
		}

		return buf.toString();
	}
}
