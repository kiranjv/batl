package com.bottlr.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.util.Log;

import com.bottlr.utils.QueryString;
import com.bottlr.utils.URLs;

public class BottlesDownloadModel extends AbstractProxy {

	private static final String TAG = "BottlesDownloadModel";
	Context context;

	public BottlesDownloadModel(Context context) {
		super(context);
		this.context = context;
	}

	public String downloadBottlesJson(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout

		QueryString queryString = new QueryString();
		// queryString.add("lat", "" + latitude);
		// queryString.add("lng", "" + longitude);
		// queryString.add("distance", "" + radius);

		
		Log.v(TAG, "URL: "+url);
		HttpGet getRequest = new HttpGet(url);
		// getRequest.setHeader("Content-Type", "application/json");

		String result = null;

		try {
			response = client.execute(getRequest);
			Log.v(TAG, "Server response: "+response);
			if (response == null) {
				
				result = null;
				failureMessage = "Server sent null response.";

			} else if (response.getStatusLine().getStatusCode() != 200) {
				response = null;
				result = null;
				failureMessage = "Bottle download fail. Response code: "
						+ response.getStatusLine().getStatusCode();
			} else {
				result = getResponseBody();
				Log.v(TAG, "Server response json: "+result);
				
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
