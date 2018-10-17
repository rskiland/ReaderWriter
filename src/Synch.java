import java.util.concurrent.*;

public class Synch {
    
    public static Semaphore mutex;		// Protects access to the counters from other threads
    									// Helps synchronizing code between threads accurately without
    									// displaying false data
    public static Semaphore ReaderSem;	// Used for Readers wait on ReaderSem semaphore
    public static Semaphore WriterSem;	// Used for Writers to wait on WriterSem semaphore
    // Counters
    public static int readersWaiting = 0;	// Current number of waiting reader threads
    public static int readersActive = 0;	// Current number of active reader threads
    public static int writersWaiting = 0;	// Current number of waiting writer threads
    public static int writersActive = 0;	// Current number of active writer threads
}

