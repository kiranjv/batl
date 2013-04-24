package com.bottlr.utils;

import com.bottlr.dataacess.BottleDetails;

public class TAGS {

	public static String BOTTLE_FOLDER = "bottles";
	public static String PATTERNS_FOLDER = "patterns";

	public static String BOTTLE_OPEN_TYPE = "open";
	public static String BOTTLE_SMALL_TYPE = "small";
	public static String BOTTLE_LARGE_TYPE = "large";

	public static String BOTTLE_OPEN_EXTENSION = "-open";
	public static String BOTTLE_SMALL_EXTENSION = "_sm";
	public static String BOTTLE_LARGE_EXTENSION = "";

	public static String PATH_SEPERATER = "/";
	public static String BOTTLE_TYPE_EXTENSION = ".png";

	public static String BOTTLE_YOUTUBE_TYPE = "youtube";
	public static String BOTTLE_VIMEO_TYPE = "vimeo";
	public static String BOTTLE_SOUNDCLOUD_TYPE = "soundcloud";
	public static String BOTTLE_SOCIALCAM_TYPE = "socialcam";
	public static final String BOTTLE_VIDDY_TYPE = "viddy";

	/** Respresent no of bottles initially download */
	public static final int BOTTLE_INITIAL_DOWNLOAD = 5;

	/** Sync bottle offset value. */
	public static final int SYNC_BOTTLE_OFFSET = 4;
	public static final long SYNC_LATEST_BOTLE_TIMER_DELAY = 5; //seconds
	public static final long SYNC_LATEST_BOTLE_TIMER_PERIOID = 1; // Minutes
	/** increments by sync offset for every old bottle update */
	public static int CURRENT_SYNC_OLD_BOTTLE_COUNT = 0;

	public static BottleDetails SELECTED_BOTTLE = null;
	public static String CURRENT_MP3_FileName = "";

}
