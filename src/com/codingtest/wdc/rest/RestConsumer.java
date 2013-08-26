package com.codingtest.wdc.rest;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.salesforce.androidsdk.rest.RestClient;

public interface RestConsumer {
	void onRestClientAvailable(RestClient client);
	void onSuccessResult(List<JSONObject> records);
	void onErrorResult(Exception exception);
	void onUpdateRecord();
}
