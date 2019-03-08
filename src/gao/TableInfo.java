package gao;

public class TableInfo {
	
	private int numTables;
	
	public TableInfo(int numTables) {
		this.numTables = numTables;
	}
	
	public synchronized void customerIn() throws InterruptedException {
		while(numTables == 0) wait();
		numTables--;
	}
	
	public synchronized void customerOut() throws InterruptedException {
		numTables++;
		notifyAll();
	}
	


}
