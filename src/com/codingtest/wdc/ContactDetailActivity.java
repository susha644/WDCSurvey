package com.codingtest.wdc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.codingtest.wdc.R;
import com.codingtest.wdc.R.id;
import com.codingtest.wdc.R.layout;
import com.salesforce.androidsdk.rest.RestClient;

/**
 * An activity representing a single contact detail screen.
 */
public class ContactDetailActivity extends SalesforceFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ContactDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ContactDetailFragment.ARG_ITEM_ID));
			ContactDetailFragment fragment = new ContactDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.contact_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, ContactListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume(RestClient client) {
		((ContactDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contact_detail_container)).onRestClientAvailable(client);
	}
}
