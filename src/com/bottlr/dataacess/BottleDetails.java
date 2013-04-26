package com.bottlr.dataacess;

import org.json.JSONException;
import org.json.JSONObject;

import com.bottlr.utils.Utils;

public class BottleDetails {
	/** bottle id */
	private String bottle_id;
	/** bottle type */
	private String botlType;
	/** Represents bottle image name */
	private String botlImageUrl;
	/** bottle created date */
	private String dateCreated;
	/** bottle distance travelled */
	private String distance;
	/** bottle top image name. */
	private String imageName;

	/** bottle like count value */
	private String likeCount;

	/** bottle views count value */
	private String locationsCount;

	/** bottle messages */
	private String message;

	/** bottle title */
	private String title;

	/** bottle username */
	private String username;

	/** bottle video name */
	private String vidUrl;

	/** bottle video type i.e youtube or vimeo */
	private String videoType;

	/** bottle video id */
	private String videoid;

	/** bottle header image full length url */
	private String full_top_image_url;

	/** bottle video full length url */
	private String full_video_url;

	/** bottle audio full length url */
	private String full_audio_url;

	/** bottle audio name or id */
	private String audio_url;

	/** bottle video type i.e youtube or vimeo */
	private String vidfrom;

	/** bottle profile imiage full length location */
	private String avatar_img;

	/** bottle pattern img name */
	private String pattern_url;

	/** bottle user real name */
	private String real_name;

	/** bottle date message */
	private String bottled_date_msg;

	/** bottle audio from details */
	private String audio_from;
	
	/** bottle created time */
	private String createdAt;

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
			String vidfrom, String avatar_img, String pattren_url,
			String real_name, String bottled_date_msg, String audio_from, String createdAt) {
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
		this.avatar_img = avatar_img;
		this.pattern_url = pattren_url;
		this.real_name = real_name;
		this.bottled_date_msg = bottled_date_msg;
		this.audio_from = audio_from;
		this.createdAt = createdAt;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the avatar_img
	 */
	public String getAvatar_img() {
		return avatar_img;
	}

	/**
	 * @param avatar_img
	 *            the avatar_img to set
	 */
	public void setAvatar_img(String avatar_img) {
		this.avatar_img = avatar_img;
	}

	/**
	 * @return the pattern_url
	 */
	public String getPattern_url() {
		return pattern_url;
	}

	/**
	 * @param pattern_url
	 *            the pattern_url to set
	 */
	public void setPattern_url(String pattern_url) {
		this.pattern_url = pattern_url;
	}

	/**
	 * @return the real_name
	 */
	public String getReal_name() {
		return real_name;
	}

	/**
	 * @param real_name
	 *            the real_name to set
	 */
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	/**
	 * @return the bottled_date_msg
	 */
	public String getBottled_date_msg() {
		return bottled_date_msg;
	}

	/**
	 * @param bottled_date_msg
	 *            the bottled_date_msg to set
	 */
	public void setBottled_date_msg(String bottled_date_msg) {
		this.bottled_date_msg = bottled_date_msg;
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

	/**
	 * @return the audio_from
	 */
	public String getAudio_from() {
		return audio_from;
	}

	/**
	 * @param audio_from
	 *            the audio_from to set
	 */
	public void setAudio_from(String audio_from) {
		this.audio_from = audio_from;
	}

	@Override
	public String toString() {
		String data = "BottleId: " + this.bottle_id + " botlType: "
				+ this.botlType + " likeCount:" + likeCount
				+ " locationsCount: " + locationsCount + " full video url: "
				+ full_video_url + " full_image_url: " + full_top_image_url
				+ " full audio url: " + full_audio_url + " real name: "
				+ real_name + " bottle date msg: " + bottled_date_msg;
		return data;
	}
	
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof BottleDetails) {
			BottleDetails bottle = (BottleDetails) object;
			String id = bottle.getBottle_id();
			String currt_id = this.getBottle_id();
			return (id == currt_id)||(id.equalsIgnoreCase(currt_id));
		}

		return object == this;
	}

}
