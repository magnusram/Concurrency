
public class OrderlyExecution {

	public static void main(String[] args) {

		OrderlyExecution order = new OrderlyExecution(); //Using the 'order' object for locking 

		Thread even = new Thread(order.new Even(order)); //Create Even number printing thread
		Thread odd = new Thread(order.new Odd(order));   //Create Odd number printing thread

		even.start();
		odd.start();

	}	
	
	class Even implements Runnable {

		final OrderlyExecution lock;

		Even(OrderlyExecution lock) {
			this.lock = lock;
		}

		public void run() {

			for (int j = 0; j < 20; j = j + 2) {
				
				synchronized (lock) {					
					System.out.printf("%d ", j);
					
					//Notify order thread and wait during each iteration
					try {
						lock.notify(); 
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} // synchronization block ends
			} // loop ends

		}

	}

	class Odd implements Runnable {

		OrderlyExecution lock;

		Odd(OrderlyExecution lock) {
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
