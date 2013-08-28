package com.codingtest.wdc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codingtest.wdc.R;
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
	 * The content this fragment is presenting.
	 */
	private static final String OBJECT_TYPE = "Contact";
	private static final List<String> FIELD_LIST = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		add("Id");
		add("Firstname");
		add("Lastname");
		add("Account.name");
		add("Title");
		add("Email");
		add("Phone");
		add("Question_1__c");
		add("Question_2__c");
		add("Question_3__c");
	}};

	private View mRootView;
	private String mContactId = null;
	private RestClient mClient = null;
	private ProgressDialog mProgressDialog = null;

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
			// Load the content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mContactId = getArguments().getString(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// get the detail fragment
		mRootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
		
		// get the submit button and set onClick listener
		Button submitButton = (Button) mRootView.findViewById(R.id.submit_button);
		submitButton.setOnClickListener(this);
		
		return mRootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(getActivity());
		Log.i(TAG, "Activity attached");
	}
	
	public void setContactId(String contactId) {
		this.mContactId = contactId;
	}

	@Override
	public void onRestClientAvailable(RestClient client) {
		this.mClient = client;
		
		// Hide the greeting text on the detail as the list item is selected
		TextView greetingText = (TextView) mRootView.findViewById(R.id.detail_greeting);
		greetingText.setVisibility(View.INVISIBLE);
		
		// make  a rest call to to retrieve 
		RestUtil.getSObject(client, getString(R.string.api_version), OBJECT_TYPE, mContactId, FIELD_LIST, this);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Loading contact details...");
        mProgressDialog.show();
	}

	@Override
	public void onSuccessResult(List<JSONObject> records) {
		if (mProgressDialog.isShowing()) 
			mProgressDialog.dismiss();
		
		Contact contact = new Contact(records.get(0));
		
		// set the details of the contact on the detail side
		setContactDetailsOnUI(contact);
	}
	
	private void setContactDetailsOnUI(Contact contact)
	{
		LinearLayout linearLayout = (LinearLayout) mRootView.findViewById(R.id.detail_top_layout);
		linearLayout.setVisibility(View.VISIBLE);
		
		TextView accountView = (TextView) mRootView.findViewById(R.id.account_detail_label);
		TextView titleView = (TextView) mRootView.findViewById(R.id.title_detail_label);
		TextView emailView = (TextView) mRootView.findViewById(R.id.email_detail_label);
		TextView phoneView = (TextView) mRootView.findViewById(R.id.phone_detail_label);
		EditText question1View = (EditText) mRootView.findViewById(R.id.question_1_hint);
		EditText question2View = (EditText) mRootView.findViewById(R.id.question_2_hint);
		EditText question3View = (EditText) mRootView.findViewById(R.id.question_3_hint);

		accountView.setText(contact.getAccount());
		titleView.setText(contact.getTitle());
		emailView.setText(contact.getEmail());
		phoneView.setText(contact.getPhone());
		question1View.setText(contact.getQuestion1());
		question2View.setText(contact.getQuestion2());
		question3View.setText(contact.getQuestion3());
	}

	@Override
	public void onErrorResult(Exception exception) {
		String msg = "Error occurred while fetching contacts";
		Log.e(TAG, msg, exception);
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();	
	}
	
	@Override
	public void onUpdateRecord() {
		String msg = "Contact survey has been updated";
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();		
	}
	
	@Override
	public void onClick(View v) {
		EditText question1EditText = (EditText) mRootView.findViewById(R.id.question_1_hint);
		EditText question2EditText = (EditText) mRootView.findViewById(R.id.question_2_hint);
		EditText question3EditText = (EditText) mRootView.findViewById(R.id.question_3_hint);
		
		Map<String, Object> surveyValues = new HashMap<String, Object>();
		surveyValues.put("Question_1__c", question1EditText.getText().toString());
		surveyValues.put("Question_2__c", question2EditText.getText().toString());
		surveyValues.put("Question_3__c", question3EditText.getText().toString());

		
		RestUtil.updateSObject(mClient, getString(R.string.api_version), OBJECT_TYPE, mContactId, surveyValues, this);

	}
}
