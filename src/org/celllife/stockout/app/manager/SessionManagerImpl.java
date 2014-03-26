package org.celllife.stockout.app.manager;

import java.util.Date;

public class SessionManagerImpl implements SessionManager {

	public static final long SESSION_TIMEOUT = 15 * 60 * 1000; // milliseconds (15 minutes)

	private String username;
	private String password;

	private Date sessionStart;
	private static Date lastInteraction;

	@Override
	public void markInteraction() {
		lastInteraction = new Date();
	}

	@Override
	public void authenticated(String username, String password) {
		this.username = username;
		this.password = password;
		if (sessionStart == null) {
			sessionStart = new Date();
		}
	}

	@Override
	public boolean isSessionExpired() {
		Date now = new Date();
		long sessionLength = now.getTime() - lastInteraction.getTime(); 
		if (sessionStart == null 
				|| lastInteraction == null
				|| sessionLength > SESSION_TIMEOUT) {
			invalidateSession();
			return true;
		}
		return false;
	}

	@Override
	public Date getLastInteractionDate() {
		return lastInteraction;
	}

	@Override
	public Date getSessionStartDate() {
		return sessionStart;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	private void invalidateSession() {
		sessionStart = null;
		lastInteraction = null;
		username = null;
		password = null;
	}
}