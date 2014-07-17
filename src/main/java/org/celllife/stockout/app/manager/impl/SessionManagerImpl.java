package org.celllife.stockout.app.manager.impl;

import java.util.Date;

import org.celllife.stockout.app.manager.SessionManager;
import org.celllife.stockout.app.ui.activities.MainActivity;

public class SessionManagerImpl implements SessionManager {

	public static final long SESSION_TIMEOUT = 15 * 60 * 1000; // milliseconds (15 minutes)

	private String username;
	private String password;
	
	private MainActivity mainActivity;

	private Date sessionStart;
	private static Date lastInteraction;

	@Override
	public void markInteraction() {
		lastInteraction = new Date();
	}

	@Override
	public void authenticated(String username, String   password) {
		this.username = username;
		this.password = password;
		if (sessionStart == null) {
			sessionStart = new Date();
		}
		lastInteraction = new Date();
	}

	@Override
	public boolean isSessionExpired() {
		Date now = new Date();
		long sessionLength = SESSION_TIMEOUT + 1;
		if (lastInteraction != null) {
			sessionLength = now.getTime() - lastInteraction.getTime(); 
		}
		if (sessionStart == null || sessionLength > SESSION_TIMEOUT) {
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
//		username = null;
//		password = null;
	}

    @Override
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity; 
    }

    @Override
    public MainActivity getMainActivity() {
        return mainActivity;
    }
}