package com.cheaptickets.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Airporthelper {
	
	private static Map<String, String> airportMapper;
	
	static{
		airportMapper = new HashMap<String, String>();
		Properties properties = new Properties();
		try {
			ClassLoader loader = Airporthelper.class.getClassLoader(); 
			java.net.URL resourceURL =loader.getResource("airportCodeToNameConvertor.properties");
			String fileToLoad = resourceURL.getPath();
		    properties.load(new FileInputStream(fileToLoad));
		} catch (IOException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("rawtypes")
		Enumeration e = properties.propertyNames();

	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      airportMapper.put(properties.getProperty(key).substring(1,4),key);
	    }
	    Collections.unmodifiableMap(airportMapper);
	}
	
	public static Map<String,String> getAirportMapper(){
		return airportMapper;
	}

}
