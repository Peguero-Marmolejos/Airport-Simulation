package Server;

import java.util.Vector;

public class Clock extends Thread{
	 public static long time = System.currentTimeMillis();
	 public void msg(String m) {
	 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	 }
	 
	 Vector flightAttendant = new Vector();
		
	 Object airplane = new Object();
	 
	 public static boolean depart = false;
		public Clock(Object airplane) {
			this.airplane = airplane;
		}//Clock

		public void callFlightAttendant() {
			try {
				sleep(5000);
				msg("************************************* 30 minutes before Flight!");
				Locks.callAttendant();
			}
			catch (Exception e) {}
		}
			
		public void ready_to_land() {
				try {
					if(depart) {
						//sleep(5000);
						msg("Prepare for Departure");
						flightAttendant.elementAt(0).notify();
					}
				}catch(Exception e) {}
				System.out.println();
				System.out.println();
		}
		public void run() {
			msg("Welcome t the Airport!");
			callFlightAttendant();
			//ready_to_land();
			}
}