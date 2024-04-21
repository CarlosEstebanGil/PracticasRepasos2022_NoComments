import java.util.concurrent.Semaphore;

public class Volatile {

	// 1) asi nomas sin controlar nada del acceso concurrente paralelo

	// Resultado ej i vale: 1554 , etc . Lo que esperaba . Se pisan x usar rec var
	// compart en caches locales threads sin sync!!

	/*
	 * static int i=0;
	 * 
	 * public Volatile() { // TODO Auto-generated constructor stub }
	 * 
	 * public static void main(String[] args) throws Exception{ //Volatile v = new
	 * Volatile();
	 * 
	 * Thread t1 = new Thread( new Runnable() {
	 * 
	 * @Override public void run() { int x=1; while (x<1000) { i = i +1; x++; } } }
	 * ); Thread t2 = new Thread (getThread2());
	 * 
	 * t1.start(); t2.start(); t1.join(); t2.join(); //Thread.sleep(100);
	 * System.out.println("i vale: " + i); }
	 * 
	 * private static Runnable getThread2() { return () -> { int x=1; while (x<1000)
	 * { i = i +1; x++; } }; }
	 */

// ____________________________________________________________________________________________________________________________

	// 2) con volatile

	// Resultado ej i vale: 1590 , etc . Lo que esperaba . Se pisan x q ambos
	// threads modifican el rec var compart
	// ( esta vez no en caches locales pero la dif de tiempo entre leer y modif con
	// threads sin sync x +q el
	// recurso sea volatile no sirve. Volatile es +xa cuando 1 solo thread modifica
	// y 1o+ (el resto) enn // SOLO necesitan
	// usar ese valor y al - volatile a esos entonces le dá la ultima vers
	// directamente desde mem y no desde sus caches, x lo
	// q si el thread modificar (x ej en un bucle etc , q sigue modif (actualizando
	// ese valor) los cons al - q son solo usuarios
	// de solo lectura del rec compartido obtienen de mem directa y no de sus caches
	// este con el valor posta ya q el t modificador
	// tmb lo actualiza directa a mem y no en su cache. y asi los usuarios siempre
	// que lean en cualquier instancia de su ejec
	// van a leer el ultimo valor. pero solo eso. no mas. es xxa ese caso nomas. es
	// una sync debil. y solo sirve xa ese caso 1-n
	// ( 1 y solo 1 !! puede modif el rec compart o produce y 1omas lo consumen (lo
	// usan , siempre actualizado, pero readonly)

	// Obs: es el mismo code que en 1) pero solo que i es volatile, no cambia nada,
	// lo dejo igual.
	/*
	 * static volatile int i=0;
	 * 
	 * public Volatile() { // TODO Auto-generated constructor stub }
	 * 
	 * public static void main(String[] args) throws Exception{ //Volatile v = new
	 * Volatile();
	 * 
	 * Thread t1 = new Thread( new Runnable() {
	 * 
	 * @Override public void run() { int x=1; while (x<1000) { i = i +1; x++; } } }
	 * ); Thread t2 = new Thread (getThread2());
	 * 
	 * t1.start(); t2.start(); t1.join(); t2.join(); //Thread.sleep(100);
	 * System.out.println("i vale: " + i); }
	 * 
	 * private static Runnable getThread2() { return () -> { int x=1; while (x<1000)
	 * { i = i +1; x++; } }; }
	 */

	// ____________________________________________________________________________________________________________________________
	// 3) intento de version synchronizada (synchronized ) ( asi solo no queda
	// synchronizado !!! )

	// Resultado ej i vale: 129! , etc . Ni con sync y volatil!. Se pisan x usar rec
	// var compart en caches locales threads sin sync!!

	/*
	 * static volatile int i=0;
	 * 
	 * public Volatile() { // TODO Auto-generated constructor stub }
	 * 
	 * public static void main(String[] args) throws Exception{ //Volatile v = new
	 * Volatile();
	 * 
	 * Thread t1 = new Thread( new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * int x=1; while (x<1000) { synchronized (this) { i = i +1; } x++; }
	 * 
	 * } } ); //Thread t2 = new Thread (getThread2()); Thread t2= new Thread( new
	 * Runnable() {
	 * 
	 * @Override public void run() { int x=1; while (x<1000) { synchronized (this) {
	 * i = i +1; } x++; } } } );
	 * 
	 * t1.start(); t2.start(); t1.join(); t2.join(); //Thread.sleep(5000);
	 * System.out.println("i vale: " + i); }
	 * 
	 * private static Runnable getThread2() {
	 * 
	 * return () -> { int x=1; while (x<1000) { i = i +1; x++; } }; }
	 */

	
	// ____________________________________________________________________________________________________________________________
	// 4) Segundo intento de version synchronizada (synchronized ) ( asi solo no queda
	// synchronizado !!! )

	// Resultado ej i vale: 1180! , etc . Ni con sync y volatil haciendo el setter y getter y sync tmb!. Se pisan x 
	// modificar en // mismo rec compartido , mas alla de todo lo puesto. obviamente hay algo q no me cierra de comofuncaesto
	

	
	// static volatile int i = 0;
	// public static synchronized int getI() {return i;}
	// public static synchronized void setI(int val) {i=val;}
	
	
	// pruebo otra cosa xa ver nomas:  incI() ( ESTO SI FUNCIONA) porque: 
	//	Teoria synchronized:
	//		" Los métodos sincronizados en Java solo pueden tener un hilo ejecutándose dentro de ellos al mismo tiempo. "
	//
	// ( los intentos comentados aca tmpoco andaban x eso probé la teoria basica y funciona, pero yo quiero modif de una y desde
	//		los propios threads. ) ent: comento todo esto de nuevo y vuelta a otro intento, un intento 5)
	/*
	 * static int i = 0; public static synchronized void incI() {i=i+1;}
	 * 
	 * public Volatile() { // TODO Auto-generated constructor stub }
	 * 
	 * public static void main(String[] args) throws Exception { // Volatile v = new
	 * Volatile();
	 * 
	 * Thread t1 = new Thread(new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * int x = 1; while (x < 1000) { synchronized (this) { incI(); //setI(getI() +
	 * 1); //i = i + 1; } x++; }
	 * 
	 * } }); // Thread t2 = new Thread (getThread2()); Thread t2 = new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { int x = 1; while (x < 1000) { synchronized
	 * (this) { incI();//Volatile.setI(getI() + 1);//i = i + 1; } x++; } } });
	 * 
	 * t1.start(); t2.start(); t1.join(); t2.join(); // Thread.sleep(5000);
	 * System.out.println("i vale: " + i); }
	 */
	
	// ____________________________________________________________________________________________________________________________
	// 5) quinto intento de version synchronizada (synchronized ) ( asi solo no queda
	// synchronizado !!! )

	// AL FIN FUNCA!! ( y sin centralizar en un metodo sync en la clase static sino modif en cada hilo de una ) Y NI HACE 
	//	FALTA Q LA VAR SEA VOLATILE! con semaforos 2 mets modif 1 rec pero nunca a la vez hacen esa parte critica, y como encima
	//	esa parte critica la tomo propietaria gracias al sem.aquire (q es un wait loop h' q se pueda yl aquire ) y la ejec 
	//	y nadie q espere por el mismo sem ( ningun otro code de ningun otro hilo x+ q en // ) pueden avanzar con su parte 
	//	"semaforizada" critica, porque otro tiene el sem, yl lo libera y ahi si el loop del aquire lo toma y ese otro enn espera
	//	pasa a usarlo y eso garantiza q esa var o rec compartido , el cod q le puse entre semaforo, apropiativo, lo altera
	//	todo el tiempo q quiere hasta q libera el sem ent los otros ya tienen ese val alterado y no se mezclan no se pisan las 
	//	posibles sentencias de separacion de las ops! y ni requiere volatile xq tmb contemplan al igual q sync creo, q 
	//	luego de ejec eso entre sem, ya afecta la realidad de una ( la mem ppal) x eso los otros threads toman los vals posta
	//	y no de un cache (creo) . osea q o centralizo afuera con un static sync con el code de modif o uso sems xa garantizar
	//	q se ejecuten esas porciones una a la vez y no en paralelo y con datos reales. ( recordar q ponerle sinc a los code 
	//	de los hilos solo hacia q ese code pueda ser ejec x 1 , pero y el otro hilo tmb podia ser ejec x 1, osea q eso de sync
	//	es mas xa centralizado o si yo solo modif un rec ovar compart critico/a creo, sino sems en cada hilo. creo. ver q el 
	//	sem es como un sem en la vida real! " un flag con espera " indep del recurso q sea (1 calle, 1 via, 1 avenida, 1 juego etc)

	static int i = 0;//static int i = 0;
	static Semaphore mutex = new Semaphore(1);
	
	//public static synchronized void incI() {i=i+1;} 
	
	public Volatile() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// Volatile v = new Volatile();
		
		
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int x = 1;
					while (x < 1000) {
						mutex.acquire();
						//synchronized (this) {
							 i++;//incI(); //setI(getI() + 1); //i = i + 1;
						//}
							 mutex.release();
						x++;
					}
				} catch (Exception e) {
					
				}							

			}
		});
		// Thread t2 = new Thread (getThread2());
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int x = 1;
					while (x < 1000) {
						mutex.acquire();
						//synchronized (this) {
							 i++;//incI(); //setI(getI() + 1); //i = i + 1;
						//}
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
		// Thread.sleep(5000);
		System.out.println("i vale: " + i);
	}

	

	
}
