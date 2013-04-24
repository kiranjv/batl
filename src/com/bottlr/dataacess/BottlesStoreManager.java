package com.bottlr.dataacess;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class BottlesStoreManager {

	private static BottlesStoreManager bottle_store = null;
	private static Context context;

	private ArrayList<BottleDetails> bottles_list = new ArrayList<BottleDetails>();

	private BottlesStoreManager(Context context) {
		this.context = context;
	}

	public static BottlesStoreManager getStoreInstance(Context context) {

		if (bottle_store == null) {
			bottle_store = new BottlesStoreManager(context);
		}
		BottlesStoreManager.context = context;
		return bottle_store;
	}

	public void storeBottlesLast(List<BottleDetails> bottles) {
		for (int i = 0; i < bottles.size(); i++) {
			BottleDetails bottleDetails = bottles.get(i);
			storeBottleLast(bottleDetails);
		}
	}

	public void storeBottleLast(BottleDetails bottle) {
		bottles_list.add(bottle);
	}

	public void storeBottlesFront(List<BottleDetails> bottles) {
		for (int i = 0; i < bottles.size(); i++) {
			BottleDetails bottleDetails = bottles.get(i);
			storeBottleFront(bottleDetails);
		}
	}

	public void storeBottleFront(BottleDetails bottle) {
		bottles_list.add(0, bottle);
	}

	/**
	 * @return the bottles_list
	 */
	public ArrayList<BottleDetails> getBottles_list() {
		return bottles_list;
	}

	/**
	 * @param bottles_list
	 *            the bottles_list to set
	 */
	public void setBottles_list(ArrayList<BottleDetails> bottles_list) {
		this.bottles_list = bottles_list;
	}
	
	public BottleDetails getTopBottle() {
		BottleDetails bottle = bottles_list.get(0);
		return bottle;
	}
	
	
	public BottleDetails getLastBottle() {
		BottleDetails bottle = bottles_list.get(bottles_list.size() - 1);
		return bottle;
	}
	public String getTopBottleCreatedAtTime() {
		BottleDetails bottle = bottles_list.get(0);
		return bottle.getCreatedAt();
	}
	
	
	public String getLastBottleCreatedAtTime() {
		BottleDetails bottle = bottles_list.get(bottles_list.size() - 1);
		return bottle.getCreatedAt();
	}

}
