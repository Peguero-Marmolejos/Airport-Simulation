package Server;

import java.util.Random;
import java.util.Vector;

public class CheckCounterClerk extends Thread{
	
	public static int counterNum = 0 ; //keeps track of the number of counters
	public static int passengersServed = 0;//keeps track of passengers served.
	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	

	public CheckCounterClerk(int name) {
		setName("Check Counter Clerk " + name);
		 counterNum++;
	
	}
	
	public void giveBoardingPass() {
		Locks.giveBoardingPass(this);
	}
	
	public void run(){
		
	}
}
		

