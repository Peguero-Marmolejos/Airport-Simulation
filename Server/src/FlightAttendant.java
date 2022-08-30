package Server;

public class FlightAttendant extends Thread{
	
	public static boolean planesReady = false;
	public static boolean board = false;
	
	public static Passenger [] plane = new Passenger[30];
	public static int passengersAboard = 0;
	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	public FlightAttendant(int name) {//, Object one, Object two, Object three,Object airplane, Clock clock) {
		if(name>=10) {
			setName("FlightAttendant " + name);
		}else {
			setName("FlightAttendant0"+name);
		}
	}

	public void waitingForPlane() {
		while(board == false) {
			msg(" is waitintg for the plane to arrive at the gate.");
			Locks.waitOnClock();
		}
	
		Locks.callPassengers1(this);
		Locks.callPassengers2(this);
		Locks.callPassengers3(this);
	}
	

	
	public void flight(){
			if(!planesReady) {}
			System.out.println();
			msg("THE DOOR FOR THE PLANE IS NOW CLOSING.");
			System.out.println();
	}
	
	public void depart() {
		
			try {
				sleep(5000);
			}catch (InterruptedException e) {e.printStackTrace();}
				System.out.println();
				msg("DEPARTING BEGINS");
				/*System.out.println();
				for(int i =0; i < plane.length; i++) {
					if(plane[i] != null) {
						plane[i].depart=true;
					}
				}/* THIS CODE DOES NOT WORK*/
		//	}
	}
	
	public void run() {
		waitingForPlane();
		flight();
	}
}
