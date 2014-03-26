package org.celllife.stockout.app.manager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import org.celllife.stockout.app.database.PhoneTableAdapter;
import org.celllife.stockout.app.domain.Phone;
import org.celllife.stockout.app.integration.rest.framework.RestAuthenticationException;
import org.celllife.stockout.app.integration.rest.framework.RestClientRunner;
import org.celllife.stockout.app.integration.rest.framework.RestCommunicationException;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AuthenticationManagerImpl implements AuthenticationManager {

	@Override
	public boolean authenticate(String username, String password) {
		// get the user cached in the app database
		Phone currentUser = getCachedPhoneDetails();
		Log.d("AuthenticationManager", "AM: found cache currentUser="+currentUser);
		if (currentUser == null || currentUser.getEncryptedPassword() == null || currentUser.getSalt() == null) {
			// retrieve the user details from the server (and cache the details in the database)
			currentUser = getUserDetailsFromServer(currentUser, username, password);
			Log.d("AuthenticationManager", "AM: found server currentUser="+currentUser);
		}
		if (currentUser.getEncryptedPassword() != null && currentUser.getSalt() != null) {
			// check if the encrypted passwords match
			boolean isValid = isValidUserPassword(currentUser, password);
			if (!isValid) {
				// FIXME: if they don't match, then perhaps they have changed their password on the server ...
			}
			Log.d("AuthenticationManager", "AM: isValid="+isValid);
			return isValid;
		} else {
			// if we still don't know the encrypted password & salt at this stage (due to server communication 
			// problems), just deny access
			// reasons: you need a connection when setting up the phone & if you have already set up the phone
			// and the cached password doesn't match what has been entered, then they probably just made a mistake
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
	
	private Phone getUserDetailsFromServer(Phone existingDetails, String username, String password) {
		Phone userDetails = existingDetails;
		if (userDetails == null) {
			userDetails = new Phone();
			userDetails.setMsisdn(username);
		}
		// FIXME: this should be done in a more central place
		// Note: this is being done on the main thread because we can't authenticate the user until it has finished.
		RestClientRunner restClientRunner = new RestClientRunner(username, password);
		String url = ManagerFactory.getSettingManager().getServerBaseUrl() + "service/users?msisdn="+username;
		try {
			String response = restClientRunner.doGet(url);
			if (response != null) {
				JSONObject user = new JSONObject(response);
				userDetails.setEncryptedPassword(user.getString("encryptedPassword"));
				userDetails.setSalt(user.getString("salt"));
				userDetails.setClinicCode(user.getString("clinicCode"));
				userDetails.setClinicName(user.getString("clinicName"));
				DatabaseManager.getPhoneTableAdapter().insertOrUpdate(userDetails);
			} else {
				// FIXME: check for errors and report back to the user
				Log.w("AuthenticationManager", "Did not get any response");
			}
		} catch (RestCommunicationException e) {
			e.printStackTrace();
		} catch (RestAuthenticationException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
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
