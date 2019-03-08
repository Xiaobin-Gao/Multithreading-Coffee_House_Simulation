package gao;

import java.util.Comparator;
import java.util.List;

public class Order implements Comparator<Order> {
	
	public List<Food> food;
	public int priority;
	public int orderNum;
	private boolean completed = false;
	
	public Order(){}
	
	public Order(List<Food> food, int priority, int orderNum) {
		this.food = food;
		this.priority = priority;
		this.orderNum = orderNum;
	}
	
	public synchronized void sendCompleted() {
		completed = true;
		notify();
	}
	
	public synchronized void checkIfCompleted() throws InterruptedException {
		while(completed == false) wait();
	}

	@Override
	public int compare(Order o1, Order o2) {
		// TODO Auto-generated method stub
		if(o1.priority > o2.priority) return 1;
		if(o1.priority < o2.priority) return -1;
		return 0;
	}

}
