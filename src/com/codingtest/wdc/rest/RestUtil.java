package com.codingtest.wdc.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class RestUtil {
	public static void sendRequest(RestClient client, String soql, String apiVersion, final RestConsumer consumer) {
		
		try {
			RestRequest restRequest = RestRequest.getRequestForQuery(apiVersion, soql);
			
			client.sendAsync(restRequest, new AsyncRequestCallback() {
				@Override
				public void onSuccess(RestRequest request, RestResponse result) {
					try {
						JSONArray jsonArray = result.asJSONObject().getJSONArray("records");
						List<JSONObject> records = new ArrayList<JSONObject>();
						for ( int index = 0; index < jsonArray.length(); index++) {
								records.add(jsonArray.getJSONObject(index));
						}

						consumer.onSuccessResult(records);
					} catch (Exception e) {
						onError(e);
					}
				}
				
				@Override
				public void onError(Exception exception) {
					consumer.onErrorResult(exception);
				}
			});

		} catch (UnsupportedEncodingException uee) {
			consumer.onErrorResult(uee);
		}
	}
	
	public static void getSObject(RestClient client, String apiVersion, String objectType, String objectId, List<String> fieldList, final RestConsumer consumer) {
		try {
			RestRequest restRequest = RestRequest.getRequestForRetrieve(apiVersion, objectType, objectId, fieldList);
			
			client.sendAsync(restRequest, new AsyncRequestCallback() {
				
				@Override
				public void onSuccess(RestRequest request, RestResponse response) {
					try {
						consumer.onSuccessResult(Collections.singletonList(response.asJSONObject()));
					} catch (Exception e) {
						onError(e);
					}
				}
				
				@Override
				public void onError(Exception exception) {
					consumer.onErrorResult(exception);
				}
			});
			
		} catch (UnsupportedEncodingException uee) {
			consumer.onErrorResult(uee);
		}
	}

}
