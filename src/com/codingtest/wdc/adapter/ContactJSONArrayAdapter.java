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
		} else {
			textView = (TextView) convertView;
		}
		if(position % 2 == 0)
			textView.setBackgroundColor(ctx.getResources().getColor(android.R.color.darker_gray));
		else 
			textView.setBackgroundColor(ctx.getResources().getColor(android.R.color.white));
		
		textView.setHeight(70);
		textView.setText(getItem(position).getName());
	
		return textView;
	}
}
