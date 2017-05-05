
public class TaskCancellation {

	public static void main(String[] args) {
		Thread mainThread = Thread.currentThread();
		/*
		 * This is the task, that would throw exception and subsequently interrupt the
		 * calling thread. That is, this thread. Since, wait() method below
		 * responds to interruptions we can act on the interruption request. For
		 * interrupting this thread, task thread should get the reference to
		 * this thread (the caller) and hence a reference is passed to the task's
		 * constructor
		 */
		Task task = new Task(mainThread);

		// Task started
		new Thread(task).start();

		try {
			/*
			 * Wait till the task completes/errors out. Calling wait()
			 * on any object requires the calling code to first acquire the lock on
			 * that object and hence we synchronize on the main thread object, which
			 * was also passed to the task thread.
			 */
			synchronized (Thread.currentThread()) {

				Thread.currentThread().wait();
			}

		} catch (InterruptedException e) {
			if (task.throwable != null) {
				System.out.println(task.throwable);
			}

		}

	}

	private static class Task implements Runnable {
		private Throwable throwable;
		private Thread callingThread;

		public Task(Thread t) {
			this.callingThread = t;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				throw new Exception("Exception in cancellable task. Interuppted after 5 seconds");
			} catch (Exception ex) {
				this.throwable = ex;
			} finally {
				callingThread.interrupt();
			}

		}
	}

}
