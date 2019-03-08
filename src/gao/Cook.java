package gao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cooks are simulation actors that have at least one field, a name.
 * When running, a cook attempts to retrieve outstanding orders placed
 * by Eaters and process them.
 */
public class Cook extends Thread {
	private final String name;
	private final int numOrdersHandled;
	private List<Order> orderReceived;
	private final Machine grill;
	private final Machine frier;
	private final Machine star;

	/**
	 * You can feel free modify this constructor.  It must
	 * take at least the name, but may take other parameters
	 * if you would find adding them useful. 
	 *
	 * @param: the name of the cook
	 */
	public Cook(String name, int numOrdersHandled, Machine grill, Machine frier, Machine star) {
		this.name = name;
		this.numOrdersHandled = numOrdersHandled;
		this.grill = grill;
		this.frier = frier;
		this.star = star;
		
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows.  The cook tries to retrieve
	 * orders placed by Customers.  For each order, a List<Food>, the
	 * cook submits each Food item in the List to an appropriate
	 * Machine, by calling makeFood().  Once all machines have
	 * produced the desired Food, the order is complete, and the Customer
	 * is notified.  The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some
	 * other thread calls the interrupt() method on it, which could
	 * raise InterruptedException if the cook is blocking), then it
	 * terminates.
	 */
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {
			while(true) {
				//YOUR CODE GOES HERE...
				if(isInterrupted()) throw new InterruptedException(); 
				orderReceived = new ArrayList<Order>();
				for(int i = 0; i < numOrdersHandled; i++) {
					Order o = Simulation.orders.poll();
					if (o == null) break;
					orderReceived.add(i, o);
				}
				if(orderReceived != null) {
					orderReceived.sort(new Order()); // sort Orders so as to handle them according to their priority
				}
				for(int i = 0; i < orderReceived.size(); i++) {
					Order o = orderReceived.get(i);
					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, o.food, o.orderNum));
					Iterator<Food> iterator = o.food.iterator();
					while (iterator.hasNext()) {
						switch(iterator.next().cookTimeMS){
							case 500:
								grill.makeFood(this, o.orderNum);
								Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.burger, o.orderNum));
								break;
							case 350:
								frier.makeFood(this, o.orderNum);
								Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.fries, o.orderNum));
								break;
							case 100:
								star.makeFood(this, o.orderNum);
								Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.coffee, o.orderNum));
								break;
						}
					}
					o.sendCompleted();
					Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, o.orderNum));
				}	
				 // sleep state makes InterruptedException happen when a Thread is interrupted   
			}
		}
		catch(InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
}