package com.cheaptickets.implementor;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cheaptickets.datatype.DestinationCost;

public class DestinationCostAggregator extends DefaultHandler {
	
	DestinationCost currentDestObj;
	List<DestinationCost> aggregator = new ArrayList<DestinationCost>();
	String tempVal;
	boolean morePending = false;
	
	public DestinationCost getCurrentDestObj() {
		return currentDestObj;
	}


	public void setCurrentDestObj(DestinationCost currentDestObj) {
		this.currentDestObj = currentDestObj;
	}


	public List<DestinationCost> getAggregator() {
		return aggregator;
	}


	public void setAggregator(List<DestinationCost> aggregator) {
		this.aggregator = aggregator;
	}


	public boolean isMorePending() {
		return morePending;
	}


	public void setMorePending(boolean morePending) {
		this.morePending = morePending;
	}


	//Event Handlers
	public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("trip")) {
			//create a new instance of employee
			currentDestObj = new DestinationCost();
			//currentDestObj.setType(attributes.getValue("type"));
		}
	}


	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}

	public void endElement(String uri, String localName,
		String qName) throws SAXException {

		if(qName.equalsIgnoreCase("trip")) {
			//add it to the list
			aggregator.add(currentDestObj);

		}else if (qName.equalsIgnoreCase("airline_display")) {
			currentDestObj.setAirline(tempVal);
		}else if (qName.equalsIgnoreCase("price")) {
			currentDestObj.setPrice(Integer.parseInt(tempVal));
		}else if (qName.equalsIgnoreCase("morepending")) {
			morePending = Boolean.valueOf(tempVal);
		}else if(qName.equalsIgnoreCase("orig")){
			currentDestObj.setDestination(tempVal);
		}

	}

}
