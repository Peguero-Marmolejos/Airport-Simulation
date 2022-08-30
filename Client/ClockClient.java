import java.io.*;
import java.net.*;

public class ClockClient extends Thread {
	public static long time = System.currentTimeMillis();
	private static int portNumber;
	private static String address;
	
	//for making a connection between client and server
	Socket connection;
	DataOutputStream DOS;
	DataInputStream  DIS;
	String response;

	public ClockClient(int port, String address) {
		this.portNumber=port;
		this.address = address;
	}
	
	public void run (){
		 try{   
			 	connection = new Socket( address, port );//create the connection between output and input
			    DOS = new DataOutputStream ( connection.getOutputStream() );
			    DIS = new DataInputStream (connection.getInputStream() );
	            msg("Connected"); 
	            DOS.writeUTF("clock");//send message to server, said this is the clock.
	            response = DIS.readUTF();//read the respond from server when clock's work finished.
	            msg("Respond: "+resPONSE);
	            DOS.close();
	            DIS.close();
	        } 
	        catch(Exception u){ 
	            System.out.println(u); 
	        } 
	}
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] " + "Clock client" + ": " + m);
	}
}

