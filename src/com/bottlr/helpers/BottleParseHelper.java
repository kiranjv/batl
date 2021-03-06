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
import com.bottlr.utils.URLs;
import com.bottlr.utils.Utils;

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
			JSONArray usersArray = main_json.getJSONArray("users");
			Log.v(TAG, "Number of bottles: " + bottlesArray.length());
			for (int i = 0; i < bottlesArray.length(); i++) {
				JSONObject botlJsonObject = bottlesArray.getJSONObject(i);
				int user_id = botlJsonObject.getInt("u_index");
				JSONObject userJsonObject = usersArray.getJSONObject(user_id);
				// parsing bottle details from bottle json
				BottleDetails bottle = parseBottleDetails(botlJsonObject,
						userJsonObject);

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

	public BottleDetails parseBottleDetails(JSONObject json_bottle,
			JSONObject userJsonObject) throws JSONException {
		String bottle_id = json_bottle.getString("bid");
		int aid = Integer.parseInt(json_bottle.getString("aid"));
		String botlType = json_bottle.getString("botlType");
		String botlImageUrl = json_bottle.getString("botlImageUrl");
		String dateCreated = json_bottle.getString("dateCreated");
		String distance = preProcessDistance(json_bottle.getString("distance"));
		String imageName = json_bottle.getString("imageName");
		String likeCount = "0";
		if (!json_bottle.isNull("likes")) {
			likeCount = json_bottle.getJSONObject("likes").getString(
					"likeCount");
		}

		String locationsCount = json_bottle.getString("locationsCount");
		String message = json_bottle.getString("message");
		String title = json_bottle.getString("title");
		String username = json_bottle.getString("username");
		String videoType = "YouTube";
		String videoid = json_bottle.getString("vidUrl");

		String full_top_image_url = "";
		String full_video_url = "";
		String full_audio_url = "";
		String audio_from = Utils.getJsonValue(json_bottle, "audiofrom");
		String audio_url = "";
		String vidfrom = "";
		String vidUrl = "";
		String avatar_img = URLs.AvatarImageBaseURL
				+ userJsonObject.getString("avatarSm");
		String real_name = userJsonObject.getString("realname");
		String pattren_url = json_bottle.getString("patternUrl");
		String bottled_date_msg = "bottled " + dateCreated;
		String createdAt = Utils.getJsonValue(json_bottle, "createdAt");
		if (!json_bottle.isNull("reBotld")) {
			try {
				JSONObject jsonObject = json_bottle.getJSONObject("reBotld");
				String uname = Utils.getJsonValue(jsonObject, "name");
				if (uname == null || uname.equalsIgnoreCase(""))
					bottled_date_msg = "rebottled " + dateCreated
							+ " by botlking";
				else
					bottled_date_msg = "rebottled " + dateCreated + " by "
							+ uname;
			} catch (Exception e) {

			}
		}

		/* Image bottle */
		if (botlType == "image" || botlType.equalsIgnoreCase("image")) {

			imageName = json_bottle.getString("imageName");

			if (!json_bottle.isNull("audiourl_url")
					&& !json_bottle.getString("audiourl_url").equalsIgnoreCase(
							""))
				audio_url = json_bottle.getString("audiourl_url");
			else if (!json_bottle.isNull("soundcloud_url")
					&& !json_bottle.getString("soundcloud_url")
							.equalsIgnoreCase(""))
				audio_url = json_bottle.getString("soundcloud_url");
			else if (aid > 0)
				audio_url = String.valueOf(aid);
		}
		/* ImageUrl type bottle */
		else if (botlType == "imageurl"
				|| botlType.equalsIgnoreCase("imageurl")) {
			imageName = json_bottle.getString("imageurl_image");
			if (!json_bottle.isNull("audiourl_url")
					&& !json_bottle.getString("audiourl_url").equalsIgnoreCase(
							""))
				audio_url = json_bottle.getString("audiourl_url");
			else if (!json_bottle.isNull("soundcloud_url")
					&& !json_bottle.getString("soundcloud_url")
							.equalsIgnoreCase(""))
				audio_url = json_bottle.getString("soundcloud_url");
			else if (aid > 0)
				audio_url = String.valueOf(aid);

		}

		/* video type bottle */
		else if (botlType == "video" || botlType.equalsIgnoreCase("video")) {
			imageName = json_bottle.getString("imageName");
			vidfrom = Utils.getJsonValue(json_bottle, "vidfrom");
			if (vidfrom.equalsIgnoreCase("")) {
				vidfrom = "Youtube";

			}
			vidUrl = json_bottle.getString("vidUrl");

		}

		/* audio type bottle */
		else if (botlType == "audio" || botlType.equalsIgnoreCase("audio")) {
			if (aid > 0) {
				audio_url = String.valueOf(aid);
			} else if ((!json_bottle.isNull("audiourl_url") && !json_bottle
					.getString("audiourl_url").equalsIgnoreCase(""))
					|| (json_bottle.isNull("soundcloud_url") && !json_bottle
							.getString("soundcloud_url").equalsIgnoreCase(""))) {

				audio_url = (!json_bottle.isNull("audiourl_url") && !json_bottle
						.getString("audiourl_url").equalsIgnoreCase("")) ? json_bottle
						.getString("audiourl_url") : json_bottle
						.getString("soundcloud_url");
			}

			if (!json_bottle.isNull("imageName")
					&& !json_bottle.getString("imageName").equalsIgnoreCase("")) {
				imageName = json_bottle.getString("imageName");

			} else if (!json_bottle.isNull("imageurl_image")
					&& !json_bottle.getString("imageurl_image")
							.equalsIgnoreCase("")) {
				imageName = json_bottle.getString("imageurl_image");

			}

		}

		else if (botlType == "AudioUrl"
				|| botlType.equalsIgnoreCase("AudioUrl")) {

			if (!json_bottle.isNull("audiourl_url")) {
				if (!json_bottle.getString("audiourl_url").equalsIgnoreCase(""))
					audio_url = json_bottle.getString("audiourl_url");
			} else if (!json_bottle.isNull("soundcloud_url")) {
				if (!json_bottle.getString("soundcloud_url").equalsIgnoreCase(
						""))
					audio_url = json_bottle.getString("soundcloud_url");
			}

			if (!json_bottle.isNull("imageName")
					&& !json_bottle.getString("imageName").equalsIgnoreCase("")) {
				imageName = json_bottle.getString("imageName");

			} else if (!json_bottle.isNull("imageurl_image")
					&& !json_bottle.getString("imageurl_image")
							.equalsIgnoreCase("")) {
				imageName = json_bottle.getString("imageurl_image");

			}

		} else if (botlType == "widget" || botlType.equalsIgnoreCase("widget")) {
			imageName = json_bottle.getString("imageurl_image");
		}

		/* generate urls to corresponding type */
		if (botlType != null && botlType.equalsIgnoreCase("widget")) {
			Log.v(TAG, "widget image url:" + imageName);
			full_top_image_url = imageName;
		} else if (!imageName.equalsIgnoreCase("")
				|| !imageName.equalsIgnoreCase("null"))

			full_top_image_url = Utils.generateLargeTopImage(imageName);

		if (botlType.equalsIgnoreCase("video")) {
			full_top_image_url = Utils.generateVideoThumbImgUrl(context,
					videoid, vidfrom, json_bottle);
			full_video_url = Utils.generateFullVideoUrl(videoid, vidfrom);
		}

		if (!audio_url.equalsIgnoreCase("")
				|| !audio_url.equalsIgnoreCase("null")) {
			full_audio_url = Utils.generateFullAudioUrl(audio_url);
		}

		videoType = vidfrom;
		/* End of parsing details */
		Log.v(TAG, "--------------------------------");
		Log.v(TAG, "bottle id: " + bottle_id);
		Log.v(TAG, "bottle type: " + botlType);
		Log.v(TAG, "imageName: " + imageName);
		Log.v(TAG, "audio_url: " + audio_url);
		Log.v(TAG, "audio_from: " + audio_from);
		Log.v(TAG, "vidfrom: " + vidfrom);
		Log.v(TAG, "vidUrl: " + vidUrl);
		Log.v(TAG, "full_top_image_url: " + full_top_image_url);
		Log.v(TAG, "full_video_url: " + full_video_url);
		Log.v(TAG, "full_audio_url: " + full_audio_url);
		Log.v(TAG, "Avatar location: " + avatar_img);
		Log.v(TAG, "Pattren url: " + pattren_url);
		Log.v(TAG, "Real name: " + real_name);
		Log.v(TAG, "bottled date msg: " + bottled_date_msg);
		Log.v(TAG, "Created At: " + createdAt);
		Log.v(TAG, "--------------------------------");

		return new BottleDetails(bottle_id, botlType, botlImageUrl,
				dateCreated, distance, imageName, likeCount, locationsCount,
				message, title, username, videoid, vidUrl, videoType,
				full_top_image_url, full_video_url, full_audio_url, audio_url,
				vidfrom, avatar_img, pattren_url, real_name, bottled_date_msg,
				audio_from, createdAt);

	}

	private String preProcessDistance(String dist) {
		int ldist = Integer.parseInt(dist);
		if (ldist > 1000) {
			int ddist = ldist / 1000;
			String rdist = (ldist % 1000) + "";
			
			dist = ddist + "." + rdist.charAt(0) + "k";
		}
		return dist;
	}

}
