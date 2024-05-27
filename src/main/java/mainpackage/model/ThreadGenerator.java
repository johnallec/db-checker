package mainpackage.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadGenerator {
	
	private ExecutorService es;
	
	private static ThreadGenerator tg = null;
	
	public static ThreadGenerator getTG() {
		if(tg == null)
			tg = new ThreadGenerator();
		return tg;
	}
	
	private ThreadGenerator() {
		es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
	}
	
	public void runJob(Runnable job) {
		es.execute(job);
	}

}
