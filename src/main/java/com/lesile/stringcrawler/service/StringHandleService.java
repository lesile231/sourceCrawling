package com.lesile.stringcrawler.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;

public class StringHandleService {

	
	/**
	 * Stream에서 읽어온 소스를 정리하는 메소드
	 * @param inputStreamReader
	 * @param outputType
	 * @return
	 */
	public LinkedList<Character> stringHandling( InputStreamReader inputStreamReader, String outputType ) {

		LinkedList<Character> lowerCaseList = new LinkedList<Character>();
		LinkedList<Character> upperCaseList = new LinkedList<Character>();
		LinkedList<Character> numericCaseList = new LinkedList<Character>();
		LinkedList<Character> characterString = new LinkedList<Character>();

		
		/**
         * 출력 타입 구분
         */
        if( outputType.equals("HTML") ) {
        		
        		try {
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
        		catch( Exception e ) {
        			e.printStackTrace();
        		}
	        
        }
        else if( outputType.equals("TEXT") ) {
        		
    			String crawlingSource = "";
    			
    			try( BufferedReader bufferedReader = new BufferedReader(inputStreamReader) ) {
    				
    				String line;
        			while ((line = bufferedReader.readLine()) != null ) {
	    	        		line = line.replaceAll("<\\/?[^>]+(>|$)", "").trim();
	    	        		crawlingSource += line;
        			}
        			
        			for( int i=0; i<crawlingSource.length(); i++ ) {
        				char ch = crawlingSource.charAt(i);
	        				
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
    			catch( Exception e ) {
    				e.printStackTrace();
    			}
    			
        }
        
        Collections.sort( upperCaseList );
		Collections.sort( lowerCaseList );
		Collections.sort( numericCaseList );
		
		characterString = this.setCharacterCaseMix(upperCaseList, lowerCaseList);
		characterString = this.setCharacterCaseMix(characterString, numericCaseList);
		
		return characterString;
	}
	
	
	
	
	/**
	 * 두개의 List의 요소를 하나씩 섞어서 새로운 List를 만드는 메소드
	 * 
	 * @param firstString
	 * @param secondString
	 * @return
	 */
	public LinkedList<Character> setCharacterCaseMix( LinkedList<Character> firstString,LinkedList<Character> secondString ) {
		
		LinkedList<Character> result = new LinkedList<Character>();
		
		try {
			
			if( firstString.size() >= secondString.size() ) {
				
				int maxLength = firstString.size();
				int minLength = secondString.size();
				
				for( int i=0; i<minLength; i++ ) {
					result.add( firstString.get(i) );
					result.add( secondString.get(i) );
				}

				
				/**
				 * 크기가 더 커서 남아있을 List의 index 설정
				 */
				int restOneIndex = minLength+1;
				
				for( int i=restOneIndex; i< maxLength; i++ ) {
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
				
				
				/**
				 * 크기가 더 커서 남아있을 List의 index 설정
				 */
				int restOneIndex = minLength+1;
				
				for( int i=restOneIndex; i< maxLength; i++ ) {
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
