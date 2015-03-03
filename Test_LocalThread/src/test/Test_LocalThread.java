package test;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

 
/**
 * Class which only contains ThreadLocal related operations
 * 
 * @author Waynner
 */
class HttpClientUtil { 

	/**
	 * 希望可以讓不同的Thread, 取得到不同的HttpClient物件. 
	 * 換言之, 同Thread也會取到同一httpClient object
	 */
	public static ThreadLocal<HttpClient> local = new ThreadLocal<HttpClient>();

	/*
	 * Get object in ThreadLocal
	 */
	public static HttpClient getLocal() {
		return local.get();
	}

	/*
	 * Set object in ThreadLocal
	 */
	public static void setLocal(HttpClient httpClient) {
		local.set(httpClient);
	}
}


/**
 * 
 * @author Waynner
 */
class ThreadTest implements Runnable {

	// Private method
	private void testMethod() {

		/* Get Thread's Name */
		String currentThread = Thread.currentThread().getName();
		System.out.println(currentThread + " runing");

		HttpClient client = HttpClientUtil.getLocal();
		if (client == null) {
			HttpClientUtil.setLocal(new DefaultHttpClient(null, null));
			System.out.println(currentThread + " set the httpClient into Threadlocal");
		} else {
			System.out.println( currentThread + " thread already had HttpClient obj in ThreadLocal pool");
		}
	}

	@Override
	public void run() {

		testMethod(); // First time is to set the HttpClient in ThreadLocal
		testMethod(); // Second time is to check the effect by threadLocal
	}
}



public class Test_LocalThread {
	
	public static void main(String[] args) {

		ThreadTest tt = new ThreadTest();

		Thread t1 = new Thread(tt, "thread 1");
		Thread t2 = new Thread(tt, "thread 2");
		// Thread t3=new Thread(tt,"thread 3");

		// t1.setPriority(Thread.MAX_PRIORITY);
		t1.start(); // Thread 1 to Call run() in ThreadTest class
		t2.start(); // Thread 2 to Call run() in ThreadTest class
		// t3.start();
	}
}

