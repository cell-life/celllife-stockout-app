package org.celllife.stockout.app.test;

import junit.framework.Assert;

import org.celllife.stockout.app.manager.AuthenticationManager;
import org.celllife.stockout.app.manager.ManagerFactory;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class AuthenticationManagerTest extends AndroidTestCase {

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ManagerFactory.initialise(context);
    }

    public void testAuthenticateTrue() {
    	AuthenticationManager authManager = ManagerFactory.getAuthenticationManager();
    	boolean success = authManager.authenticate("0768198075", "1234");
    	Assert.assertTrue(success);
    }

    public void testAuthenticateFalse() {
    	AuthenticationManager authManager = ManagerFactory.getAuthenticationManager();
    	boolean success = authManager.authenticate("0768198075", "12345");
    	Assert.assertFalse(success);
    }

    public void tearDown() throws Exception{
        super.tearDown();
    }
}