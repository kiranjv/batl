package com.bottlr.helpers;

import java.util.List;

import com.bottlr.dataacess.BottleDetails;
import com.bottlr.network.BottlesDownloadModel;
import com.bottlr.utils.UIUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncBottleDownload extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = "AsyncBottleDownload";

	public static boolean isDownloading = false;
	private Context context;
	private int bottle_count;
	private ListRowItemsAdapter list_adapter;
	List<BottleDetails> parsedBottles;
	private String failureMSG;

	public AsyncBottleDownload(Context context, int bottle_count) {
		this.context = context;
		
		this.bottle_count = bottle_count;
	}

	@Override
	protected void onPreExecute() {
		isDownloading = true;
		super.onPreExecute();
	}

	/**
	 * asyncronus background service to download the bottles from bottle server.
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		BottlesDownloadModel download = new BottlesDownloadModel(context);
		String bottles = download.downloadBottlesJson(bottle_count);
		if (bottles == null) {
			failureMSG = download.getFailureMessage();
			return false;
		}

		BottleParseHelper bottlesParser = new BottleParseHelper(context);
		parsedBottles = bottlesParser.parseBottles(bottles);
		Log.e(TAG, "Downloaded bottles size: " + parsedBottles.size());
		Log.e(TAG, "Downloaded bottles json: " + bottles);
		bottlesParser.storeBottleLocal(parsedBottles);
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (!result) {
			// UIUtils.OkDialog(context,
			// "No bottles from server. Message is "+failureMSG);
			Toast.makeText(context,
					"No bottles from server. Message is " + failureMSG,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					context,
					"Bottle download sucess." + parsedBottles.size()
							+ " bottles downloaded.", Toast.LENGTH_SHORT)
					.show();
		}

		isDownloading = false;
		super.onPostExecute(result);
	}

	/**
	 * @return the parsedBottles
	 */
	public List<BottleDetails> getParsedBottles() {
		return parsedBottles;
	}

	/**
	 * @param parsedBottles
	 *            the parsedBottles to set
	 */
	public void setParsedBottles(List<BottleDetails> parsedBottles) {
		this.parsedBottles = parsedBottles;
	}

}
