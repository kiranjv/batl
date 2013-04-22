package com.bottlr.helpers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadThreadPollHelper {

	private static ExecutorService executor = null;

	private DownloadThreadPollHelper() {}
	
	public static ExecutorService getExcecutor() {
         if(executor == null)
        	 executor = Executors.newFixedThreadPool(5); 
		
		return executor;
		
	}
	
	
}
