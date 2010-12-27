package com.cheaptickets.implementor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cheaptickets.datatype.DestinationCost;
import com.cheaptickets.datatype.RequestObject;
import com.cheaptickets.descriptors.QueryTickets;
import com.cheaptickets.helper.Airporthelper;

public class QueryTicketsFromKayak implements QueryTickets {
	
	//CONSTANTS
	String hostname="http://www.kayak.com";
	String token="8l1A_fmWv2OGrR3xrFruTQ";
	String sessionid="";
	String searchId="";
	char searchtype='\0';
	int lastcount=-1;

	@Override
	public List<String> getCurrentTicketCosts(RequestObject request) {
		sessionid= getsession();
		List<String> output = new ArrayList<String>();
		List<DestinationCost> finalResults = new ArrayList<DestinationCost>();
		for(String airportCode : Airporthelper.getAirportMapper().keySet()){
			if(request.getAirportName().equalsIgnoreCase(airportCode))
				continue;
			searchId = start_flight_search(request.getAirportName(), airportCode, "01/03/2011", "01/05/2011");
			DestinationCostAggregator aggregator = pollResults();
			boolean more = aggregator != null ? aggregator.isMorePending() : false;
			finalResults.addAll(aggregator.getAggregator());
			while (more) {
				aggregator = pollResults();
				more = aggregator.isMorePending();
				finalResults.addAll(aggregator.getAggregator());
			}
		}
		Collections.sort(finalResults);
		for(DestinationCost dest : finalResults)
			output.add(dest.toString());
		return output;
	}
	
	@Override
	public String getsession(){
		String response=fetch(hostname+"/k/ident/apisession?token=" + token );
		Element root=xmlRoot(response);
		NodeList sessionid=root.getElementsByTagName("sid");
		if( sessionid.getLength() == 0){
			System.out.println("BAD TOKEN: " + response);
			System.exit(1);
		}
		return sessionid.item(0).getFirstChild().getNodeValue();
	}
	
	private String fetch(String urlstring){
		  String content = "";
	  
		  try {
			URL url  = new URL(urlstring);  
	        InputStream is = url.openStream();
	        BufferedReader d = new BufferedReader(new InputStreamReader(is));
	        String s;
	        
	        while (null != (s = d.readLine())) {
	            content = content + s + "\n";
	        }
	        is.close();
	        
		  } catch ( Exception e ) { System.out.println(e.getMessage() ); }
		  return content;
	    }
	
	private Element xmlRoot(String response){
		Document doc =null;
		try {
		     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = dbf.newDocumentBuilder();
		     ByteArrayInputStream bais = new ByteArrayInputStream(response.getBytes());
		     doc = db.parse(bais);
		    } catch (Exception e) {
		      System.out.print("Problem parsing the xml: \n" + e.toString());
		}
		    
		return doc.getDocumentElement();
		
	}
	
	private String start_flight_search(String origin, String destination, 
			String dep_date , String ret_date) {
		String url=hostname +"/s/apisearch?basicmode=true&oneway=n&origin=" +origin +
			"&destination=" +destination + "&destcode=&depart_date="+ dep_date +
			"&depart_time=a&return_date=" + ret_date + "&return_time=a&travelers=1" +
			"&cabin=e&action=doflights&apimode=1&_sid_=" + sessionid;
		return start_search(url);
		
	}
	
	private String start_search(String uri){
		String response= fetch(uri);
		Element root= xmlRoot(response);
		NodeList searchid=root.getElementsByTagName("searchid");
		if( searchid.getLength() == 0){
			System.out.println("SEARCH ERROR: \n" + response);
			System.exit(1);
		}
		return searchid.item(0).getFirstChild().getNodeValue();
	}
	
	private DestinationCostAggregator pollResults(){
		String uri= hostname +"/s/apibasic/flight?searchid="+searchId+"&apimode=1&_sid_="+sessionid ; 
		String response=fetch(uri);	
		return handleResults(response);
	}
	
	private DestinationCostAggregator handleResults(String response){
		DestinationCostAggregator aggregator = parseResponse(response);
		return aggregator;
	}
	
	private DestinationCostAggregator parseResponse(String response){
		SAXParserFactory saf = SAXParserFactory.newInstance();
		DestinationCostAggregator aggregator = new DestinationCostAggregator();
		try {
			SAXParser sp = saf.newSAXParser();
			sp.parse(new StringInputStream(response), aggregator);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aggregator;
	}

}
