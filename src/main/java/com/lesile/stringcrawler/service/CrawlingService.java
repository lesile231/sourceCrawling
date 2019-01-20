package com.lesile.stringcrawler.service;

import java.net.HttpURLConnection;
import java.net.URL;

public class CrawlingService {
	
	
	public HttpURLConnection getConnectionFromURL(String url) {
		
		HttpURLConnection conn;
		
		try {
			URL inputURL = new URL(url);
			
			conn = (HttpURLConnection) inputURL.openConnection();
	        conn.setRequestMethod("GET");
	        
			return conn;
		}
		catch( Exception e ) {
			return null;
		}
	}
	
}
