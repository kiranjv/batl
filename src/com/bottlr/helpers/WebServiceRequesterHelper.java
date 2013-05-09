package com.bottlr.helpers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.Log;

import com.bottlr.network.AbstractProxy;
import com.bottlr.utils.QueryString;
import com.bottlr.utils.TAGS;
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;

public class WebServiceRequesterHelper extends AbstractProxy {

	private static WebServiceRequesterHelper instance = null;

	private WebServiceRequesterHelper(Context context) {
		super(context);
	}

	public static WebServiceRequesterHelper getInstance(Context context) {

		if (instance == null)
			instance = new WebServiceRequesterHelper(context);

		return instance;

	}

	private static final String TAG = "WebServiceRequesterHelper";
	private String failureMessage;
	static private final Logger logger = LoggerFactory
			.getLogger(WebServiceRequesterHelper.class);

	public String getMP3AudioAPI(String audio_id) {
		
		String url = URLs.BOTTLE_URL_AUDIO_API + audio_id;
		Log.v(TAG, "API Request Site: " + url);

		String responseData = postRequest(url);
		Log.v(TAG, "Response: "+responseData);
		logger.debug("Response: "+responseData);
		if (responseData != null) {
			// parse main_thumb key value
			JSONObject main_json;
			JSONObject audiodata_json = null;
			String audio_url = null;
			try {
				main_json = new JSONObject(responseData);
				audiodata_json = main_json.getJSONObject("audiodata");
				String filename = Utils.getJsonValue(audiodata_json, "url");
				TAGS.CURRENT_MP3_FileName = filename;
				audio_url = URLs.BOTTLE_DIRECT_AUDIO_BASE_URL + filename;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			return audio_url;
		}

		return null;
	}

	public String getSocailCamThumbImageAPI(String video_id) {
		String url = "https://api.socialcam.com/v1/videos/"
				+ video_id
				+ ".json?access_token=7e72ddbe9f917c89da61e32782ba6213ce99a56fa061255e022e2e19c038c71ab0116c00b0b58c198a2c5e13886a6a40";
		Log.v(TAG, "API Request Site: " + url);

		String responseData = postRequest(url);
		if (responseData != null) {
			// parse main_thumb key value
			JSONObject main_json;
			JSONObject main_thumb_json = null;
			try {
				main_json = new JSONObject(responseData);
				main_thumb_json = main_json.getJSONObject("data")
						.getJSONObject("main_thumb");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String thumb_url = null;
			try {
				thumb_url = Utils.getJsonValue(main_thumb_json, "url");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return thumb_url;
		}
		return null;
	}

	public String postRequest(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout

		QueryString queryString = new QueryString();
		// queryString.add("lat", "" + latitude);
		// queryString.add("lng", "" + longitude);
		// queryString.add("distance", "" + radius);

		HttpGet getRequest = new HttpGet(url);
		// getRequest.setHeader("Content-Type", "application/json");

		String result = null;

		try {
			response = client.execute(getRequest);
			Log.v(TAG, "Server response: " + response);
			if (response == null) {

				result = null;
				failureMessage = "Server sent null response.";

			} else if (response.getStatusLine().getStatusCode() != 200) {
				response = null;
				result = null;
				failureMessage = "Request failled. Response code: "
						+ response.getStatusLine().getStatusCode();
			} else {
				
					result = EntityUtils.toString(response.getEntity());
					
				
//				result = getResponseBody();
//				response = null;
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
