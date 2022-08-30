package Server;

import java.net.*;
import java.io.*;

public class AirportServer extends Thread {
	private static final int portNumber = 5056;
	public static void main(String[] args) throws Exception {
		try{
			ServerSocket server = new ServerSocket(portNumber);
			int counter=0;
			System.out.println("Server Started ....");
		    while(true){
		    	counter++;
		        Socket serverClient=server.accept();  //connect to a client
		        System.out.println(" >> " + "Client No:" + counter + " has begun!");
		        AirportServerMultiThread asmt = new AirportServerMultiThread(serverClient,counter); //send  the request to a "helper" thread
		        asmt.start();
		    }//try
		}catch(Exception e){
		    System.out.println(e);
		}//catch
	}//main
}//airport Server

