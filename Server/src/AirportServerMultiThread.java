package Server;

import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class AirportServerMultiThread extends Thread{
	Socket serverClient;
	int clientNo;
	int methodNumber;
	
	Vector <Object> Pass;
	
	public static long time = System.currentTimeMillis();

	  
	  AirportServerMultiThread(Socket inSocket,int counter){
	    serverClient = inSocket;
	    clientNo=counter;
	  }
	  public void run(){
	    try{
	    	while(true) {
		      DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
		      DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
		      String clientMessage="", serverMessage="";
		      
		      while((clientMessage  = inStream.readUTF()) != null){
		        clientMessage=inStream.readUTF();
		        switch(containsType(clientMessage)) {
		        case "Passenger" :{
			        	Passenger p = new Passenger(Integer.parseInt(clientMessage.substring(9,10))); // create a Passenger thread
			        	switch(containsType(clientMessage)) {
			        	case "method-1" :{
			        		p.arrival();
				        		while (p.isAlive()) {
									Thread.sleep(5);
								}
								outStream.writeUTF("Method 1 is complete.");//when this method finished, respond to client
								continue;//method#1 means it doesn't finish yet, so we need to get next message.
			        	}
			        	case "method-2" :{
			        		p.checkInKiosk();
			        			while(p.isAlive()) {
			        				Thread.sleep(5);
			        			}
			        			outStream.writeUTF("Method 2 is complete.");
			        			continue;
			        	}
			        	case "method-3" :{
			        		p.enterGate();
			        			while(p.isAlive()) {
			        				Thread.sleep(5);
			        			}
			        			outStream.writeUTF("Method 3 is complete.");
			        			continue;
			        	}
			        	case "method-4" :{
			        		p.departure();
			        			while(p.isAlive()) {
			        				Thread.sleep(5);
			        			}
			        			outStream.writeUTF("Method 4 is complete.");
			        			outStream.writeUTF("All methods are complete for this passenger thread.");
			        			break;
			        	}
			        	}
			    }
		        case "CheckCounterClerk":{
		        	CheckCounterClerk ccc = new CheckCounterClerk(Integer.parseInt(clientMessage.substring(17,18)));// create checkcounterclerk thread
		        	//switch(containsType(clientMessage)) 
		        	//case "method-1" : {
		        		ccc.giveBoardingPass();
		        	//}
		        	outStream.writeUTF("All methods are complete for this check counter clerk.");
		        	break;	
		        }
		        case "FlightAttendent":{
		        	FlightAttendant fa = new FlightAttendant(Integer.parseInt(clientMessage.substring(15,16)));
		        	fa.start();
		        }
		        case "Clock":{
		        	Clock clock = new Clock(1);
		        	clock.run();
		        }
		        System.out.println("From Client-" +clientNo+ ": Method Number is :"+clientMessage);
		        methodNumber = Integer.parseInt(clientMessage);
		        serverMessage="From Server to Client-" + clientNo + " !!!";
		        outStream.flush();
		      }
		      inStream.close();
		      outStream.close();
		      serverClient.close();
		      }
	    	}
	    }catch(Exception ex){
	      System.out.println(ex);
	    }finally{
	      System.out.println("Client -" + clientNo + " exit!! ");
	    }
	  }
	

	public String containsType(String a) {
		String p = "Passenger", c = "Clock", ccc = "CheckCounterClerk" , fa = "FlightAttendent";
		Pattern pp = Pattern.compile("Passenger"); 
		Pattern cccp = Pattern.compile("CheckCounterClerk");
		Pattern fap = Pattern.compile("FlightAttendent");
		Pattern cp = Pattern.compile("Clock");
	
		if(pp.matcher(a).matches()) {
	    	return p;
	    }
	    if(cccp.matcher(a).matches()) {
	    	return ccc;
	    }
	    if(fap.matcher(a).matches()) {
	    	return fa;
	    }
	    if(cp.matcher(a).matches()) {
	    	return c;
	    }
	    return null;
	}

	public String containsMethod(String a) {
		String one = "method-1", two = "method-2", three = "method-3" , four = "method-4";
		Pattern onep = Pattern.compile("method-1"); 
		Pattern twop = Pattern.compile("method-2");
		Pattern threep = Pattern.compile("method-3");
		Pattern fourp = Pattern.compile("method-4");
	
		if(onep.matcher(a).matches()) {
	    	return one;
	    }
	    if(twop.matcher(a).matches()) {
	    	return two;
	    }
	    if(threep.matcher(a).matches()) {
	    	return three;
	    }
	    if(fourp.matcher(a).matches()) {
	    	return four;
	    }
	    return null;
	}
}
