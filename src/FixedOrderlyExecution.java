/*
 * Synchronize thread execution of Even and Odd threads 
 * in such a way that the numbers are printed in sequence
 * 0 1 2 3 4 ....
 * 
 * Even thread prints Even numbers 
 * Odd thread prints Odd numbers
 */

/*
 * This is the fixed version previously uploaded solutions (OrderlyExecution.java)
 * which had a bug that did not ensure that Even thread always starts first
 */
public class FixedOrderlyExecution {

	public static void main(String[] args) {

		FixedOrderlyExecution order = new FixedOrderlyExecution(); //Using the 'order' object for locking 
		
		/*
		 * Create odd thread and pass it to even thread
		 * Start Odd thread execution after Even prints '0'
		 * This will ensure that the even thread always starts first
		 */
		Thread odd = new Thread(order.new Odd(order));   

		/*
		 * Create even thread with odd thread passed in the constructor
		 */
		Thread even = new Thread(order.new Even(order,odd)); //Create Even number printing thread	

		/*
		 * Start the even thread
		 */
		even.start();

	}	
	
	class Even implements Runnable {

		final FixedOrderlyExecution lock;
		final Thread odd;

		Even(FixedOrderlyExecution lock, Thread odd) {
			this.lock = lock;
			this.odd = odd;
		}

		public void run() {

			for (int j = 0; j < 20; j = j + 2) {
				
				synchronized (lock) {					
					System.out.printf("%d ", j);
					
					//Notify order thread and wait during each iteration
					try {	
						//Time to start the odd thread
						if(j==0){
							odd.start();							
						}
						lock.wait();
						lock.notify();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				} // synchronization block ends
			} // loop ends
			synchronized (lock){
				lock.notify();
			}
		}

	}

	class Odd implements Runnable {

		FixedOrderlyExecution lock;

		Odd(FixedOrderlyExecution lock) {
			this.lock = lock;
		}

		void printNumber(int i) {
			System.out.printf("%d ", i);
		}

		public void run() {
			for (int j = 1; j < 20; j = j + 2) {
				synchronized (lock) {
					System.out.printf("%d ", j);
					try {
						//Notify order thread and wait during each iteration
						lock.notify();
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} // synchronization block ends
			} // loop ends			
		}

	}
}
