package com.codingtest.wdc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codingtest.wdc.R;
import com.codingtest.wdc.model.Contact;

public class ContactJSONArrayAdapter extends ArrayAdapter<Contact> {
	private Context ctx;

	public ContactJSONArrayAdapter(Context ctx) {
		super(ctx, R.layout.contact_list_textview);
		this.ctx = ctx;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = null;
		
		if (convertView == null) {
			textView = (TextView) LayoutInflater.from(ctx).inflate(R.layout.contact_list_textview, null);
			textView.setBackgroundColor(ctx.getResources().getColor(position % 2 == 0 ? android.R.color.darker_gray : android.R.color.white));
		} else {
			textView = (TextView) convertView;
		}
		
		
		textView.setHeight(70);

		String firstName = getItem(position).getFirstName();
		String lastName = getItem(position).getLastName();
		textView.setText(lastName + ", " + firstName);
	
		return textView;
	}
}
