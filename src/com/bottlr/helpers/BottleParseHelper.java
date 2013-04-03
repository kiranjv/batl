package com.bottlr.helpers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.bottlr.dataacess.BottleDetails;
import com.bottlr.dataacess.BottlesRepository;

public class BottleParseHelper {

	private static final String TAG = "BottleParseHelper";
	private Context context;

	public BottleParseHelper(Context context) {
		this.context = context;
	}

	public List<BottleDetails> parseBottles(String data) {
		List<BottleDetails> bottle_list = new ArrayList<BottleDetails>();
		try {
			Log.v(TAG, "Response json: " + data);
			JSONObject main_json = new JSONObject(data);
			JSONArray bottlesArray = main_json.getJSONArray("botls");
			Log.v(TAG, "NUmber of bottles: " + bottlesArray.length());
			for (int i = 0; i < bottlesArray.length(); i++) {
				JSONObject jsonObject = bottlesArray.getJSONObject(i);
				BottleDetails bottle = new BottleDetails(jsonObject);
				Log.d(TAG, "Bottle " + i + ": " + bottle.toString());

				bottle_list.add(bottle);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bottle_list;
	}

	public void storeBottleLocal(List<BottleDetails> bottle_list) {
		BottlesRepository bottle_repo = new BottlesRepository(context);
		for (int i = 0; i < bottle_list.size(); i++) {
			BottleDetails bottle = bottle_list.get(i);
			bottle_repo.insertBottle(bottle);
		}
	}

}
