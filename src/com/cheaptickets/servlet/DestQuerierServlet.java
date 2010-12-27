package com.cheaptickets.servlet;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cheaptickets.datatype.RequestObject;
import com.cheaptickets.helper.Constants;
import com.cheaptickets.implementor.QueryTicketsFromKayak;

/**
 * Servlet implementation class HelloWorldServlet
 */
public class DestQuerierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DestQuerierServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html");
		
		String sourcePort = request.getParameter(Constants.SOURCE_AIRPORT);
		RequestObject queryReqObj = new RequestObject();
		queryReqObj.setAirportName(sourcePort);
		
		java.io.PrintWriter output = response.getWriter();
		//Dummy Comments
		QueryTicketsFromKayak queryObj = new QueryTicketsFromKayak();
		
		List<String> outputPrint = queryObj.getCurrentTicketCosts(queryReqObj);
		
		for(String toPrint : outputPrint){
			output.println(toPrint);
			output.println("<br>");
		}
		
		output.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
