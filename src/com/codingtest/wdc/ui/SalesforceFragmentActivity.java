package com.codingtest.wdc.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codingtest.wdc.R;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ClientManager;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.ClientManager.RestClientCallback;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.security.PasscodeManager;
import com.salesforce.androidsdk.util.EventsObservable;
import com.salesforce.androidsdk.util.EventsObservable.EventType;
import com.salesforce.androidsdk.util.TokenRevocationReceiver;

/**
 * Abstract base class for all Salesforce activities.
 */
public abstract class SalesforceFragmentActivity extends ActionBarActivity {

	private PasscodeManager passcodeManager;
    private TokenRevocationReceiver tokenRevocationReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Gets an instance of the passcode manager.
		passcodeManager = SalesforceSDKManager.getInstance().getPasscodeManager();
		tokenRevocationReceiver = new TokenRevocationReceiver(this);

		// Lets observers know that activity creation is complete.
		EventsObservable.get().notifyEvent(EventType.MainActivityCreateComplete, this);
	}

	@Override 
	public void onResume() {
		super.onResume();
		registerReceiver(tokenRevocationReceiver, new IntentFilter(ClientManager.ACCESS_TOKEN_REVOKE_INTENT));

		// Brings up the passcode screen if needed.
		if (passcodeManager.onResume(this)) {

			// Gets login options.
			final String accountType = SalesforceSDKManager.getInstance().getAccountType();
	    	final LoginOptions loginOptions = SalesforceSDKManager.getInstance().getLoginOptions();

			// Gets a rest client.
			new ClientManager(this, accountType, loginOptions, SalesforceSDKManager.getInstance().shouldLogoutWhenTokenRevoked()).getRestClient(this, new RestClientCallback() {

				@Override
				public void authenticatedRestClient(RestClient client) {
					if (client == null) {
						SalesforceSDKManager.getInstance().logout(SalesforceFragmentActivity.this);
						return;
					}
					onResume(client);

					// Lets observers know that rendition is complete.
					EventsObservable.get().notifyEvent(EventType.RenditionComplete);
				}
			});
		}
	}
	
	public abstract void onResume(RestClient client);

	@Override
	public void onUserInteraction() {
		passcodeManager.recordUserInteraction();
	}

    @Override
    public void onPause() {
        super.onPause();
    	passcodeManager.onPause(this);
    	unregisterReceiver(tokenRevocationReceiver);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salesforce_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
            	SalesforceSDKManager.getInstance().logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
