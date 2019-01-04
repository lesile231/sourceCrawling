package com.lesile.stringcrawler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CrawlingController {
	
	/**
	 * Welcome Page Controller
	 */
	@RequestMapping(value = "main.do")
	public String main (Model model) {
		
		return "main";
	}
	
	
	
	/**
	 * 입력 받은 URL로 접속해서 소스를 크롤링 해오는 Controller
	 */
	@RequestMapping(value = "crawlingFromUrl.do", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> stringProcessing (@RequestParam(value="url") String url, @RequestParam(value="outputType") String outputType, Model model) {
		
		URL inputURL;
		HttpURLConnection conn;
		BufferedReader bufferedReader;
		InputStream input;
		InputStreamReader inputStreamReader;
		
		String crawlingSource = "";
		
		LinkedList<Character> lowerCaseList = new LinkedList<Character>();
		LinkedList<Character> upperCaseList = new LinkedList<Character>();
		LinkedList<Character> numericCaseList = new LinkedList<Character>();
		LinkedList<Character> characterString = new LinkedList<Character>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			inputURL = new URL( url );
			
			conn = (HttpURLConnection) inputURL.openConnection();
	        conn.setRequestMethod("GET");
	        

	        /**
	         * 출력 타입 구분
	         */
	        if( outputType.equals("HTML") ) {
	        	
		        input = conn.getInputStream();
	        	
		        inputStreamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
		        
	        		int ch;
		        while ( (ch = inputStreamReader.read() ) != -1 ) {
		        		
		        		if( '0' <= ch && ch <= '9' ) {
		        			
		        			numericCaseList.add( (char) ch );
		        		}
		        		else if ( 'a' <= ch && ch <= 'z' ) {
		        			
		        			lowerCaseList.add( (char) ch );
		        		}
		        		else if ( 'A' <= ch && ch <= 'Z' ) {

		        			upperCaseList.add( (char) ch );
		        		}
		        }
		        
	        }
	        else if( outputType.equals("TEXT") ) {
	        		
	        		bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

	    			String line;
	    			while ( (line = bufferedReader.readLine() ) != null ) {
			        	
		        		line = line.replaceAll("<\\/?[^>]+(>|$)", "").trim();
		        		crawlingSource += line;
		        }
	    			
	    			for( int i=0; i<crawlingSource.length(); i++ ) {
	    				
	    				char ch = crawlingSource.charAt( i );
	    				
	    				if( '0' <= ch && ch <= '9' ) {
		        			
	    					numericCaseList.add( (char) ch );
		        		}
		        		else if ( 'a' <= ch && ch <= 'z' ) {
		        			
		        			lowerCaseList.add( (char) ch );
		        		}
		        		else if ( 'A' <= ch && ch <= 'Z' ) {

		        			upperCaseList.add( (char) ch );
		        		}
	    			}
	    			
	    	        bufferedReader.close();
	        }
		}
		catch ( Exception e ) {
			
			return resultMap;
		}
		
		Collections.sort( upperCaseList );
		Collections.sort( lowerCaseList );
		Collections.sort( numericCaseList );
		
		characterString = this.setCharacterCaseMix(upperCaseList, lowerCaseList);
		characterString = this.setCharacterCaseMix(characterString, numericCaseList);
		
		resultMap.put( "characterString" , characterString );
		
		return resultMap;
	}
	
	
	/**
	 * 두개의 List의 요소를 하나씩 섞어서 새로운 List를 만드는 메소드
	 * 
	 * @param firstString
	 * @param secondString
	 * @return
	 */
	private LinkedList<Character> setCharacterCaseMix( LinkedList<Character> firstString,LinkedList<Character> secondString ) {
		
		LinkedList<Character> result = new LinkedList<Character>();
		
		try {
			
			if( firstString.size() >= secondString.size() ) {
				
				int maxLength = firstString.size();
				int minLength = secondString.size();
				
				for( int i=0; i<minLength; i++ ) {
					
					result.add( firstString.get(i) );
					result.add( secondString.get(i) );
				}

				int idx = minLength+1;
				
				for( int i=idx; i< maxLength; i++ ) {
					result.add( firstString.get(i) );
				}
				
			}
			else {
				
				int maxLength = secondString.size();
				int minLength = firstString.size();
				
				for( int i=0; i<minLength; i++ ) {
					
					result.add( firstString.get(i) );
					result.add( secondString.get(i) );
				}

				int idx = minLength+1;
				
				for( int i=idx; i< maxLength; i++ ) {
					result.add( secondString.get(i) );
				}
			}
			
			return result;
		}
		catch( Exception e ) {
			return null;
		}
		
	}
}
