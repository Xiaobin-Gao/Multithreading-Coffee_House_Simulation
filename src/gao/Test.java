package gao;

import java.util.LinkedList;
import java.util.Queue;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Customer c = new Customer("hihiohi", null, 1, null);
		Queue<Customer> a = new LinkedList<Customer>();
		a.add(c);
		System.out.print(a.remove());
	}

}
