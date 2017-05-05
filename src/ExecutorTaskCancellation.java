import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorTaskCancellation {

	private static final Executor thread_pool = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		CompletionService<Throwable> cs = new ExecutorCompletionService<>(thread_pool);
		Future<Throwable> f = cs.submit(new Task());
		try {
			System.out.println(f.get().getMessage());
		} catch (InterruptedException | ExecutionException e) {
			System.out.println(e.getClass().getName() + " during task execution");
		}

	}

	private static class Task implements Callable<Throwable> {

		@Override
		public Throwable call() {
			Throwable throwable = null;
			try {
				throw new Exception("Exception thrown from the task");
			} catch (Exception e) {
				throwable = e;
			}
			return throwable;
		}
	}

}
