import java.util.concurrent.*;

public class MainMethod {
    public static void main (String argv[]) {
        
        Synch.mutex = new Semaphore(1, true);		// Protects access to four counters in Synch
        Synch.ReaderSem = new Semaphore(1, true);	// Readers wait on ReaderSem semaphore
        Synch.WriterSem = new Semaphore(1, true);   // Writers wait on WriterSem semaphore
        
        Reader R;
        Writer W;
        
        Synch.mutex.release();
        
        for (int i=1; i<=8; i++) {
            W = new Writer(i);
            W.start();
            R = new Reader(i);
            R.start();
        }
        System.out.println("This is main speaking");
    }
}
