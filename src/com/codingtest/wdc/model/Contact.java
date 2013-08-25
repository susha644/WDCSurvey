package com.codingtest.wdc.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Contact {
	
	private static final String TAG = Contact.class.getSimpleName();
	
	private final JSONObject holder;
	
	public Contact(JSONObject holder) {
		this.holder = holder;
	}
	
	public String getName() {
		return getStringPropertySafe("Name", "No Name");
	}
	
	public String getId() {
		return getStringPropertySafe("Id", "No Id");
	}

	private String getStringPropertySafe(String prop, String defValue) {
		String value = defValue;
		try {
			value = holder.getString(prop);
		} catch (JSONException e) {
			Log.e(TAG, "Error getting contact name", e);
		}
		
		return value;
	}
}
