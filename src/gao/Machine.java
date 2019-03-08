package gao;

import java.util.ArrayList;

/**
 * A Machine is used to make a particular Food.  Each Machine makes
 * just one kind of Food.  Each machine has a capacity: it can make
 * that many food items in parallel; if the machine is asked to
 * produce a food item beyond its capacity, the requester blocks.
 * Each food item takes at least item.cookTimeMS milliseconds to
 * produce.
 */
public class Machine {
	public final String machineName;
	public final Food machineFoodType;
	private int capacityLeft;
	private int capacityIn;


	//YOUR CODE GOES HERE...


	/**
	 * The constructor takes at least the name of the machine,
	 * the Food item it makes, and its capacity.  You may extend
	 * it with other arguments, if you wish.  Notice that the
	 * constructor currently does nothing with the capacity; you
	 * must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
		this.capacityIn = capacityIn;
		this.capacityLeft = capacityIn;
		//YOUR CODE GOES HERE...
	}
	

	

	/**
	 * This method is called by a Cook in order to make the Machine's
	 * food item.  You can extend this method however you like, e.g.,
	 * you can have it take extra parameters or return something other
	 * than Object.  It should block if the machine is currently at full
	 * capacity.  If not, the method should return, so the Cook making
	 * the call can proceed.  You will need to implement some means to
	 * notify the calling Cook when the food item is finished.
	 */
	public synchronized void makeFood(Cook cook, int orderNum) throws InterruptedException {
		checkCapacity();
		Simulation.logEvent(SimulationEvent.cookStartedFood(cook, machineFoodType, orderNum));
		Thread t = new Thread(new CookAnItem());
		t.start();
		t.join();
		Simulation.logEvent(SimulationEvent.machineDoneFood(this, machineFoodType));
		capacityLeft++;
		notifyAll();
	}
	
	private synchronized void checkCapacity() throws InterruptedException {
		if(capacityLeft == 0) wait();
		capacityLeft--;
	}
	

	//THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		public void run() {
			Simulation.logEvent(SimulationEvent.machineCookingFood(new Machine(machineName, machineFoodType, capacityIn), machineFoodType));
			try {
				//YOUR CODE GOES HERE...
				
				Thread.sleep(machineFoodType.cookTimeMS);
		
			} catch(InterruptedException e) { }
			Simulation.logEvent(SimulationEvent.machineDoneFood(new Machine(machineName, machineFoodType, capacityIn), machineFoodType));
		}
	}
 

	public String toString() {
		return machineName;
	}
}