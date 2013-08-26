package com.codingtest.wdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codingtest.wdc.model.Contact;
import com.codingtest.wdc.rest.RestConsumer;
import com.codingtest.wdc.rest.RestUtil;
import com.salesforce.androidsdk.rest.RestClient;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ContactDetailActivity} on handsets.
 */
public class ContactDetailFragment extends Fragment implements RestConsumer, android.view.View.OnClickListener {
	
	private static final String TAG = ContactDetailFragment.class.getSimpleName();
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	//private DummyContent.DummyItem mItem;

	private String mContactId = null;
	private Contact mContact = null;
	private RestClient mClient = null;
	
	private static final String OBJECT_TYPE = "Contact";
	private static final List<String> FIELD_LIST = new ArrayList<String>() {{
		add("Id");
		add("Name");
		add("Account.name");
		add("Title");
		add("Email");
		add("Phone");
		add("Question_1__c");
		add("Question_2__c");
		add("Question_3__c");
	}};

	private View mRootView;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ContactDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mContactId = getArguments().getString(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
		
		Button submitButton = (Button) mRootView.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(this);
		return mRootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(getActivity());
		Log.i(TAG, "Activity attached");
	}

	@Override
	public void onRestClientAvailable(RestClient client) {
		this.mClient = client;
		RestUtil.getSObject(client, getString(R.string.api_version), OBJECT_TYPE, mContactId, FIELD_LIST, this);
	}

	@Override
	public void onSuccessResult(List<JSONObject> records) {
		mContact = new Contact(records.get(0));
		
		TextView accountView = (TextView) mRootView.findViewById(R.id.accountDetail);
		TextView titleView = (TextView) mRootView.findViewById(R.id.titleDetail);
		TextView emailView = (TextView) mRootView.findViewById(R.id.emailDetail);
		TextView phoneView = (TextView) mRootView.findViewById(R.id.phoneDetail);
		accountView.setText(mContact.getAccount());
		titleView.setText(mContact.getTitle());
		emailView.setText(mContact.getEmail());
		phoneView.setText(mContact.getPhone());
	}

	@Override
	public void onErrorResult(Exception exception) {
		String msg = "Error occurred while fetching contacts";
		Log.e(TAG, msg, exception);
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();	
	}
	
	public void setContactId(String contactId) {
		this.mContactId = contactId;
	}

	@Override
	public void onClick(View v) {
		EditText question1EditText = (EditText) mRootView.findViewById(R.id.questionEditText1);
		EditText question2EditText = (EditText) mRootView.findViewById(R.id.questionEditText2);
		EditText question3EditText = (EditText) mRootView.findViewById(R.id.questionEditText3);
		
		Map<String, Object> surveyValues = new HashMap<String, Object>();
		surveyValues.put("Question_1__c", question1EditText.getText().toString());
		surveyValues.put("Question_2__c", question2EditText.getText().toString());
		surveyValues.put("Question_3__c", question3EditText.getText().toString());

		
		RestUtil.updateSObject(mClient, getString(R.string.api_version), OBJECT_TYPE, mContactId, surveyValues, this);

	}

	@Override
	public void onUpdateRecord() {
		String msg = "Contact survey has been updated";
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();		
	}
	
}
