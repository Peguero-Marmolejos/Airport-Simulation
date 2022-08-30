package Server;

	import java.util.ArrayList;
	import java.util.Random;
	import java.util.Vector;

	public class Locks {
		public static long time = System.currentTimeMillis();
		
		public Object checkIn, Zone1, Zone2, Zone3 = new Object();
		
		public static Vector <Passenger> checkInLine = new Vector();// checkIn line
		public static  Vector waitingToCheckIn = new Vector();// line for passengers on "standby"
		 
		public static Vector <Passenger>zone1 = new Vector(11);// 
		public static Vector <Passenger>zone2 = new Vector(11);// 
		public static Vector <Passenger>zone3 = new Vector(11);//
		
		public static int[] seatAvailable = new int[30];
		public static Passenger[] plane = new Passenger[30];

		public static Vector<Object> fa = new Vector<>();// a vector to store the flight attendant object

		
		public static void checkInKiosk(Passenger pass) {
			Object checkIn = new Object();
		     synchronized (checkIn) {
		    	 if (cannotCheckInNow(checkIn, pass)){
		    		 msg("will wait for next available check counter");
		    		 while (true) {// wait to be notified, not interrupted
		    			 try { checkIn.wait(); break;}// notify() after interrupt() race condition ignored
		            	 catch (InterruptedException e) { continue;}
		             }
		         }
		    	msg(" will be added to the check-in queue");
		    	checkInLine.addElement(pass);
		    	checkIn.notify();
		      }
		 }

		private static synchronized boolean cannotCheckInNow(Object convey, Passenger pass) {
			 boolean status;
		     if (checkInLine.size() == 2) {
		         waitingToCheckIn.addElement(convey);
		         status = true;
		      } else {
		         Passenger.numPassengers++;
		         status = false;
		      }
		      return status;
		 }
		 
		public static synchronized  void giveBoardingPass(CheckCounterClerk ccc) {
			Object checkIn = new Object();
			synchronized(checkIn) {
			while(checkInLine.isEmpty()) {
				msg("Is waiting for customers to arrive at counter.");
				while(true) {
					try{
						checkIn.wait();break;
					}catch(InterruptedException ie) {}
				}
			}
			Passenger pass = checkInLine.remove(0);
			
			Random random = new Random();
			int randomSeat = random.nextInt(30)+1;
			while(seatAvailable[randomSeat-1] == 1) {
				randomSeat = random.nextInt(30)+1;
			}			
			seatAvailable[randomSeat-1] = 1;// making seat taken already

			if(randomSeat !=30) {
				msg("gives " + pass.getName() + " a boarding pass : Zone : " + ((randomSeat / 10)+1) + " Seat : " + ((randomSeat % 10) + 1));
			}else {
				msg("gives " + pass.getName() + " a boarding pass : Zone : " + (randomSeat / 10) + " Seat : " + ((randomSeat % 10) + 1));
			}
			CheckCounterClerk.passengersServed++;
			pass.setBoardingPass(randomSeat);
			}
		}

		public static void enterGate(Passenger pass) {
			Object Zone1 = new Object(), Zone2 = new Object(), Zone3 = new Object();
			 if (pass.boardingPass/10 == 0) {
				 msg(" is in zone 1");
				 synchronized(Zone1) {
					 while (true) {// wait to be notified, not interrupted
						 try { 
							 zone1.add(pass);
							Zone1.wait(); 
						 	break;
						 }// notify() after interrupt() race condition ignored
						 catch (InterruptedException e) {}
					 } try{
						 msg("is putting luggage away.");
						 pass.sleep(500); // to simulate putting luggage away
						 msg("is now confortably seated on the plane.");
					 }catch(InterruptedException e) {}
				 }
			 }
			 if (pass.boardingPass/10 == 1) {
				 msg("is in zone 2");
				 synchronized(Zone2) {				 
					 while (true) {// wait to be notified, not interrupted
						 try { 
							zone2.add(pass);
						 	Zone2.wait(); 
						 	break;
						 }// notify() after interrupt() race condition ignored
						 catch (InterruptedException e) {}
					 } try{
						 msg("is putting luggage away.");
						 pass.sleep(500); // to simulate putting luggage away
						 msg("is now confortably seated on the plane.");
					 }catch(InterruptedException e) {}
				 }
			 }
			 else if(pass.boardingPass/10 >= 2) {
				 msg("is in zone 3");
				 synchronized(Zone3) {
					 while (true) {// wait to be notified, not interrupted
						 try {  
							 zone3.add(pass);
							 Zone3.wait(); 
							 break;
						 }// notify() after interrupt() race condition ignored
						 catch (InterruptedException e) {}
					 }
					 try{
						 msg("is putting luggage away.");
						 pass.sleep(500); // to simulate putting luggage away
						 msg("is now confortably seated on the plane.");
					 }catch(InterruptedException e) {}
				 }
			 }
		 
	 }
		
		public static synchronized void callAttendant() {// This one is for clock to notify the flight attendant
			synchronized (fa.elementAt(0)) {
				fa.elementAt(0).notify();
			}
			fa.removeElementAt(0);
		}
		
		public static void waitOnClock() {// This one is so the flight attendant waits for the clock
			Object convey = new Object();
			synchronized (convey) {
				try {
					fa.addElement(convey);
					convey.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		
	
		
		/*static synchronized void departure() {// Used for staff, when the staff want to wake up every audience.
			while (peopleOnSeat.size() > 0) {// notify every audience in side the vector
				synchronized (peopleOnSeat.elementAt(0)) {
					peopleOnSeat.elementAt(0).notify();
				}
				peopleOnSeat.removeElementAt(0);
			}
		}*/

		

		/*
		public static void goHomeYouMissedIt() {//At the end, the clock will use this one to release all the passengers who didnt make it to the plane before boarding time
			while (waitingGreen.size() > 0) {//release all green-uniform student
				synchronized (waitingGreen.elementAt(0)) {
					waitingGreen.elementAt(0).notify();
				}
				waitingGreen.removeElementAt(0);
			}
			while (waitingOrange.size() > 0) {//release all orange-uniform student
				synchronized (waitingOrange.elementAt(0)) {
					waitingOrange.elementAt(0).notify();
				}
				waitingOrange.removeElementAt(0);
			}
		}*/

		private static void msg(String m) {
			System.out.println("[" + (System.currentTimeMillis() - time) + "]" + m);
		}

		public static void callPassengers1(FlightAttendant fa) {
			synchronized (zone1) {
				msg("All passengers of zone 1 please, scan your ticket and begin boarding, NOW. ");
				System.out.println();
				while(!(zone1.isEmpty())) {
					int seat = zone1.elementAt(zone1.size()-1).getBoardingPass();
					plane[seat-1] = zone1.remove(zone1.size()-1);
				}
				zone1.notifyAll(); 
			} 
			try {
				fa.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public static void callPassengers2(FlightAttendant fa) {
			System.out.println();
			synchronized (zone2) {
				msg("All passengers of zone 2 please, scan your ticket and begin boarding, NOW. ");
				System.out.println();
				while(!(zone2.isEmpty())) {
					int seat = zone2.elementAt(zone2.size()-1).getBoardingPass();
					plane[seat-1] = zone2.remove(zone2.size()-1);
				}
				zone2.notifyAll(); 
			} 
			try {
				fa.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public static void callPassengers3(FlightAttendant fa) {
			synchronized (zone3) {
				System.out.println();
				msg("All passengers of zone 3 please, scan your ticket and begin boarding, NOW. ");
				System.out.println();
				while(!(zone3.isEmpty())) {
					int seat = zone3.elementAt(zone3.size()-1).getBoardingPass();
					plane[seat-1] = zone3.remove(zone3.size()-1);
				}zone3.notifyAll(); 

			} 			
			try {
				fa.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			FlightAttendant.planesReady = true;
		}
		
	}




