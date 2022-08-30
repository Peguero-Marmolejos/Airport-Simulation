package Server;

import java.util.Vector;
public class Passenger extends Thread {
	
	public static int numPassengers = 0;
	 
	public int boardingPass = -1;
	 
	public static boolean depart = false;

	public static Vector <Passenger>zone3 = new Vector(11);// */

	 public static long time = System.currentTimeMillis();
	 public void msg(String m) {
		 System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+": "+m);
	 }
	 
	 public Passenger(int name) {
			setName("Passenger " + name);
	 }
	 
	 public void arrival() {//
		 try{
			 Thread.sleep((long)(Math.random() * 1000));
			 msg("has arrived . ");
		 }catch (InterruptedException ie) {
			 msg("was interrupted");
		 }
	 }
	 
	 
	 public void setBoardingPass(int bP) {
		this.boardingPass = bP;
	 }
		 
	 public synchronized int getBoardingPass() {
		 msg("gets boarding pass scanned.");
		 return boardingPass;
	 }

	 
	 public void departure() {
		/* while(depart == false) {}
			if(depart) {msg("is in seat Z" + ((boardingPass/10)+1) + " :S " + (boardingPass%10) + " and is departing.");}
			*//* THIS CODE DOES NOT WORK*/
	 }
	 public void checkInKiosk() {
		Locks.checkInKiosk(this);
	 }
	 public void enterGate() {
			Locks.enterGate(this);
	 }

	@Override
	public void run() {
		arrival(); //sleep for a random time then arrive to Wonder_Land
		while( boardingPass < 0){}
		//flight();
		departure();// used busy wait, im sorry
		
	}
}
