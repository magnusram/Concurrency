
public class OrderlyExecution {

	public static void main(String[] args) {

		OrderlyExecution order = new OrderlyExecution();

		Thread even = new Thread(order.new Even(order));
		Thread odd = new Thread(order.new Odd(order));

		even.start();
		odd.start();

	}
	

	class Lock {

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
