package com.bottlr.dataacess;

import org.json.JSONException;
import org.json.JSONObject;

import com.bottlr.utils.Utils;

public class BottleDetails {

	private String bottle_id;
	private String botlType;
	private String botlImageUrl;
	private String dateCreated;
	private String distance;
	private String imageName;
	private String likeCount;
	private String locationsCount;
	private String message;
	private String title;
	private String username;
	private String vidUrl;
	private String videoType;

	private String videoid;

	private String full_top_image_url;

	private String full_video_url;
	private String full_audio_url;
	private String audio_url;
	private String vidfrom;

	public BottleDetails(JSONObject json_bottle) {
		// try {
		// // parseBottleJson(json_bottle);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public BottleDetails(String bottle_id, String botlType,
			String botlImageUrl, String dateCreated, String distance,
			String imageName, String likeCount, String locationsCount,
			String message, String title, String username, String videoid,
			String vidUrl, String videoType, String full_top_image_url,
			String full_video_url, String full_audio_url, String audio_url,
			String vidfrom) {
		this.bottle_id = bottle_id;
		this.botlType = botlType;
		this.botlImageUrl = botlImageUrl;
		this.dateCreated = dateCreated;
		this.distance = distance;
		this.imageName = imageName;
		this.likeCount = likeCount;
		this.locationsCount = locationsCount;
		this.message = message;
		this.title = title;
		this.username = username;
		this.videoid = videoid;
		this.vidUrl = vidUrl;
		this.videoType = videoType;
		this.full_top_image_url = full_top_image_url;
		this.full_video_url = full_video_url;
		this.full_audio_url = full_audio_url;
		this.audio_url = audio_url;
		this.vidfrom = vidfrom;

	}

	@Override
	public String toString() {
		String data = "BottleId: " + this.bottle_id + " botlType: "
				+ this.botlType + " likeCount:" + likeCount
				+ " locationsCount: " + locationsCount + " full video url: "
				+ full_video_url + " full_image_url: " + full_top_image_url
				+ " full audio url: " + full_audio_url;
		return data;
	}

	/**
	 * @return the bottle_id
	 */
	public String getBottle_id() {
		return bottle_id;
	}

	/**
	 * @param bottle_id
	 *            the bottle_id to set
	 */
	public void setBottle_id(String bottle_id) {
		this.bottle_id = bottle_id;
	}

	/**
	 * @return the botlType
	 */
	public String getBotlType() {
		return botlType;
	}

	/**
	 * @param botlType
	 *            the botlType to set
	 */
	public void setBotlType(String botlType) {
		this.botlType = botlType;
	}

	/**
	 * @return the botlImageUrl
	 */
	public String getBotlImageUrl() {
		return botlImageUrl;
	}

	/**
	 * @param botlImageUrl
	 *            the botlImageUrl to set
	 */
	public void setBotlImageUrl(String botlImageUrl) {
		this.botlImageUrl = botlImageUrl;
	}

	/**
	 * @return the dateCreated
	 */
	public String getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName
	 *            the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the likeCount
	 */
	public String getLikeCount() {
		return likeCount;
	}

	/**
	 * @param likeCount
	 *            the likeCount to set
	 */
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	/**
	 * @return the locationsCount
	 */
	public String getLocationsCount() {
		return locationsCount;
	}

	/**
	 * @param locationsCount
	 *            the locationsCount to set
	 */
	public void setLocationsCount(String locationsCount) {
		this.locationsCount = locationsCount;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the vidUrl
	 */
	public String getVidUrl() {
		return vidUrl;
	}

	/**
	 * @param vidUrl
	 *            the vidUrl to set
	 */
	public void setVidUrl(String vidUrl) {
		this.vidUrl = vidUrl;
	}

	/**
	 * @return the videoType
	 */
	public String getVideoType() {
		return videoType;
	}

	/**
	 * @param videoType
	 *            the videoType to set
	 */
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	/**
	 * @return the videoid
	 */
	public String getVideoid() {
		return videoid;
	}

	/**
	 * @param videoid
	 *            the videoid to set
	 */
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}

	/**
	 * @return the full_top_image_url
	 */
	public String getFull_top_image_url() {
		return full_top_image_url;
	}

	/**
	 * @param full_top_image_url
	 *            the full_top_image_url to set
	 */
	public void setFull_top_image_url(String full_top_image_url) {
		this.full_top_image_url = full_top_image_url;
	}

	/**
	 * @return the full_video_url
	 */
	public String getFull_video_url() {
		return full_video_url;
	}

	/**
	 * @param full_video_url
	 *            the full_video_url to set
	 */
	public void setFull_video_url(String full_video_url) {
		this.full_video_url = full_video_url;
	}

	/**
	 * @return the full_audio_url
	 */
	public String getFull_audio_url() {
		return full_audio_url;
	}

	/**
	 * @param full_audio_url
	 *            the full_audio_url to set
	 */
	public void setFull_audio_url(String full_audio_url) {
		this.full_audio_url = full_audio_url;
	}

	/**
	 * @return the audio_url
	 */
	public String getAudio_url() {
		return audio_url;
	}

	/**
	 * @param audio_url
	 *            the audio_url to set
	 */
	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}

	/**
	 * @return the vidfrom
	 */
	public String getVidfrom() {
		return vidfrom;
	}

	/**
	 * @param vidfrom
	 *            the vidfrom to set
	 */
	public void setVidfrom(String vidfrom) {
		this.vidfrom = vidfrom;
	}

}
