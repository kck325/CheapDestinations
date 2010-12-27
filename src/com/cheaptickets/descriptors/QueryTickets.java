package com.cheaptickets.descriptors;

import java.util.List;

import com.cheaptickets.datatype.RequestObject;

public interface QueryTickets {

	public List<String> getCurrentTicketCosts(RequestObject request);
	
	String getsession();
}
