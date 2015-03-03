package test;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

 
/**
 * 
 * @author Waynewym
 *
 */
class HttpClientUtil {

	/**
	 * 希望可以讓不同的Thread, 取得到不同的HttpClient物件 換言之, 同Thread也會取到同一httpClient object
	 */
	public static ThreadLocal<HttpClient> local = new ThreadLocal<HttpClient>();

	public static HttpClient getLocal() {
		return local.get();
	}

	public static void setLocal(HttpClient httpClient) {
		local.set(httpClient);
	}
}

public class Test_LocalThread {
	/**
	 * Main Enter
	 * @param args
	 */
	public static void main(String[] args) {

		ThreadTest tt = new ThreadTest();

		Thread t1 = new Thread(tt, "thread 1");
		Thread t2 = new Thread(tt, "thread 2");
		// Thread t3=new Thread(tt,"thread 3");

		// t1.setPriority(Thread.MAX_PRIORITY);
		t1.start(); // Thread 1 to Call run()
		t2.start(); // Thread 2 to Call run()
		// t3.start();
	}
}

/**
 * 
 * @author Waynner
 * 
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
			System.out.println(currentThread + " HttpClient equals null");
		} else {
			System.out
					.println("This thread had HttpClient obj in ThreadLocal pool");
		}
		// System.out.println( "ThreadLocal Get: " + client );
	}

	@Override
	public void run() {

		testMethod(); // First time is to set the HttpClient in ThreadLocal
		testMethod(); // Second time is to check the effect by threadLocal
	}

}
