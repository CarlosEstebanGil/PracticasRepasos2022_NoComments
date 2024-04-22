import java.util.concurrent.Semaphore;

public class Volatile {


	static int i = 0;
	static Semaphore mutex = new Semaphore(1);
	
	
	public Volatile() {

	}

	public static void main(String[] args) throws Exception {
		
		
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int x = 1;
					while (x < 1000) {
						mutex.acquire();

							 i++;
							 
							 mutex.release();
						x++;
					}
				} catch (Exception e) {
					
				}							

			}
		});
 
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int x = 1;
					while (x < 1000) {
						mutex.acquire();

							 i++; 
						
							 mutex.release();
						x++;
					}
				} catch (Exception e) {
					
			}							
			}
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println("i vale: " + i);
	}
	
}
