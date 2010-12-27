package com.cheaptickets.datatype;

import com.cheaptickets.helper.Airporthelper;

public class DestinationCost implements Comparable<DestinationCost>{
	int price;
	String airline;
	String destination;
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		StringBuffer temp = new StringBuffer("You can travel to ");
		temp.append(Airporthelper.getAirportMapper().get(this.destination));
		temp.append(" for ").append(this.price).append(" on ").append(this.airline);
		return temp.toString();
	}
	
	@Override
	public int compareTo(DestinationCost second) {
		if(this.price > second.price)
			return 1;
		else
			return -1;
	}
	
	
}
