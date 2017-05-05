public class TaskCancellationUsingClassObject {

	public static void main(String[] args) {
		/*
		 * This is the task, that would throw exception and subsequently notify the
		 * calling thread. That is, this thread. For notifying this thread, 
		 * task thread should lock on the same object as the 
		 * one that this thread is waiting on and hence the Class object is passed
		 * as reference to the task 
		 */
		Task task = new Task(TaskCancellationUsingClassObject.class);

		// Task started
		new Thread(task).start();

		try {
			/*
			 * Wait till the task completes/errors out. Calling wait()
			 * on any object requires the calling code to first acquire the lock on
			 * that object and hence we synchronize on the class object which was also passed to the task
			 * thread.
			 */
			synchronized (TaskCancellationUsingClassObject.class) {

				TaskCancellationUsingClassObject.class.wait();
				if (task.throwable != null) {
					System.out.println(task.throwable);
				}

			}

		} catch (InterruptedException e) {
			System.out.println("Thread interuppted");
		}

	}

	private static class Task implements Runnable {
		private Throwable throwable;
		private Object callingThread;

		public Task(Object t) {
			this.callingThread = t;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				throw new Exception("Exception in cancellable task.");
			} catch (Exception ex) {
				this.throwable = ex;
			} finally {
				synchronized(callingThread){
					callingThread.notify();
				}
			}

		}
	}


}
