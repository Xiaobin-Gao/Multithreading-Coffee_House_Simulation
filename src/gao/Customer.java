package gao;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the coffee shop (only successful if the
 * coffee shop has a free table), place its order, and then leave the 
 * coffee shop when the order is complete.
 */
public class Customer implements Runnable {
	//JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;    
	private final int priority;
	private final TableInfo tableInfo;
	
	private static int runningCounter = 0;

	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order, int priority, TableInfo tableInfo) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;
		this.priority = priority;
		this.tableInfo = tableInfo;
	}

	public String toString() {
		return name;
	}

	/** 
	 * This method defines what an Customer does: The customer attempts to
	 * enter the coffee shop (only successful when the coffee shop has a
	 * free table), place its order, and then leave the coffee shop
	 * when the order is complete.
	 */
	public void run() {
		//YOUR CODE GOES HERE...
		try {
			Simulation.logEvent(SimulationEvent.customerStarting(this));
			tableInfo.customerIn(); // 
			Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
			Order o = new Order(this.order, this.priority, this.orderNum);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, order, orderNum));
			Simulation.orders.add(o);
			o.checkIfCompleted();
			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, order, orderNum));
			Thread.sleep(300); // a Customer takes some time to eat food
			Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
			tableInfo.customerOut();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}