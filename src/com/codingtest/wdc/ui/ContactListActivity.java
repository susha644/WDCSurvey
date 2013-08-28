 package com.codingtest.wdc.ui;
/*
 * Copyright (c) 2012, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codingtest.wdc.R;
import com.codingtest.wdc.SalesforceFragmentActivity;
import com.salesforce.androidsdk.rest.RestClient;

/**
 * Main activity
 */
public class ContactListActivity extends SalesforceFragmentActivity implements ContactListFragment.Callbacks {

    // for twopane mode
	private boolean mTwoPane;
	private RestClient mRestClient;
	private ContactDetailFragment mDetailFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup view
		setContentView(R.layout.activity_contact_list);

		if (findViewById(R.id.contact_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.contact_list)).setActivateOnItemClick(true);
			
			mDetailFragment = new ContactDetailFragment();
			
			// replace fragment view once reuse it 
			getSupportFragmentManager().beginTransaction().replace(R.id.contact_detail_container, mDetailFragment).commit();
		}
		
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		
		// Hide everything until we are logged in
		if (mTwoPane) {
			findViewById(R.id.contact_detail_container).setVisibility(View.INVISIBLE);
		} else {
			findViewById(R.id.contact_list).setVisibility(View.INVISIBLE);
		}
	}		
	
	@Override
	public void onResume(RestClient client) {
		this.mRestClient = client;

		// distribute the client show everything as we are logged in
		((ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.contact_list)).onRestClientAvailable(client);
		if (mTwoPane) {
			findViewById(R.id.contact_detail_container).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.contact_list).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onContactSelected(String id) {
		if (mTwoPane) {
			mDetailFragment.setContactId(id);
			mDetailFragment.onRestClientAvailable(mRestClient);
			
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ContactDetailActivity.class);
			detailIntent.putExtra(ContactDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
