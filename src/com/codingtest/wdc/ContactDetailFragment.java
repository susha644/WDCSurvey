package com.codingtest.wdc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ContactDetailFragment extends Fragment implements RestConsumer {
	
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
	
	private static final String OBJECT_TYPE = "Contact";
	private static final List<String> FIELD_LIST = new ArrayList<String>() {{
		add("Id");
		add("Name");
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
		return mRootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(getActivity());
		Log.i(TAG, "Activity attached");
	}

	@Override
	public void onRestClientAvailable(RestClient client) {
		RestUtil.getSObject(client, getString(R.string.api_version), OBJECT_TYPE, mContactId, FIELD_LIST, this);
	}

	@Override
	public void onSuccessResult(List<JSONObject> records) {
		mContact = new Contact(records.get(0));
		
		TextView contactNameView = (TextView) mRootView.findViewById(R.id.contact_name);
		contactNameView.setText(mContact.getName());
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
	
}
