package com.lesile.stringcrawler.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lesile.stringcrawler.service.CrawlingService;
import com.lesile.stringcrawler.service.StringHandleService;


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
		
		StringHandleService stringHandleService = new StringHandleService();
		CrawlingService crawlingService = new CrawlingService();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		LinkedList<Character> crawlingStringList = new LinkedList<Character>();
		
		HttpURLConnection conn = crawlingService.getConnectionFromURL( url );
		
		if( conn == null ) {
			return null;
		}
		
		
		try (
			  InputStream inputStream = conn.getInputStream();
			  InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)
		) {
			crawlingStringList = stringHandleService.stringHandling( inputStreamReader, outputType );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		
		resultMap.put( "characterString" , crawlingStringList );
		return resultMap;
	}
	
	
}
