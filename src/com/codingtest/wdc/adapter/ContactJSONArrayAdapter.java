package com.codingtest.wdc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codingtest.wdc.model.Contact;

public class ContactJSONArrayAdapter extends ArrayAdapter<Contact> {
	private Context ctx;

	public ContactJSONArrayAdapter(Context ctx) {
		super(ctx, android.R.layout.simple_list_item_activated_1);
		this.ctx = ctx;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = null;
		
		if (convertView == null) {
			textView = (TextView) LayoutInflater.from(ctx).inflate(android.R.layout.simple_expandable_list_item_1, null);
		} else {
			textView = (TextView) convertView;
		}
		
		textView.setText(getItem(position).getName());
		
		return textView;
	}
}
