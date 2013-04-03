package com.bottlr.dataacess;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class BottlesRepository extends DBAdapter {

	public static String CREATE_QUERY = "CREATE TABLE bottle (bottle_id TEXT  NOT NULL,botlType TEXT  NOT NULL,"
			+ " botlImageUrl TEXT NULL, dateCreated TEXT NULL, distance TEXT NULL, imageName TEXT NULL, "
			+ "likeCount TEXT NULL, locationsCount TEXT NULL, message TEXT NULL, title TEXT NULL, username TEXT NULL,"
			+ " vidId TEXT NULL, vidUrl TEXT NULL, videoType TEXT NULL, videoThumbUrl TEXT NULL)";

	private String insertQuery = "INSERT INTO bottle ("
			+ "bottle_id ,botlType,"
			+ " botlImageUrl, dateCreated, distance, imageName, "
			+ "likeCount, locationsCount, message, title, username,"
			+ " vidId, vidUrl, videoType, videoThumbUrl" + ") VALUES ("
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + ")";

	private Context context;

	public BottlesRepository(Context context) {
		super(context);
		this.context = context;
	}

	public void insertBottle(BottleDetails bottle) {
		Object[] args = { bottle.getBottle_id(), bottle.getBotlType(),
				bottle.getBotlImageUrl(), bottle.getDateCreated(),
				bottle.getDistance(), bottle.getImageName(),
				bottle.getLikeCount(), bottle.getLocationsCount(),
				bottle.getMessage(), bottle.getTitle(), bottle.getUsername(),
				bottle.getVideoid(), bottle.getVidUrl(), bottle.getVideoType(),
				bottle.getVidThumbUrl() };

		this.Query(insertQuery, args);
	}

	public Cursor searchBottle(String bottleId) {
		String[] argv = { "" + bottleId };
		String selectQuery = "Select * from profiles where id =?";
		Cursor cursor = this.selectQuery(selectQuery, argv);
		this.selectQuery(selectQuery, argv).close();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

		}
		return cursor;
	}

	public ArrayList<BottleDetails> retriveBottles() {

		String[] argv = {};
		String selectQuery = "Select * from bottle";
		Cursor cursor = this.selectQuery(selectQuery, argv);

		return convertToBottles(cursor);
	}

	private ArrayList<BottleDetails> convertToBottles(Cursor cursor) {
		ArrayList<BottleDetails> bottles = new ArrayList<BottleDetails>();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				String bottle_id = cursor.getString(0);
				String botlType = cursor.getString(1);
				String botlImageUrl = cursor.getString(2);
				String dateCreated = cursor.getString(3);
				String distance = cursor.getString(4);
				String imageName = cursor.getString(5);
				String likeCount = cursor.getString(6);
				String locationsCount = cursor.getString(7);
				String message = cursor.getString(8);
				String title = cursor.getString(9);
				String username = cursor.getString(10);
				String vidId = cursor.getString(11);
				String vidUrl = cursor.getString(12);
				String videoType = cursor.getString(13);
				String videoThumbUrl = cursor.getString(14);
				BottleDetails bottle = new BottleDetails(bottle_id, botlType,
						botlImageUrl, dateCreated, distance, imageName,
						likeCount, locationsCount, message, title, username,
						vidId, vidUrl, videoType, videoThumbUrl);
				bottles.add(bottle);
			}
		}
		cursor.close();
		return bottles;
	}
}