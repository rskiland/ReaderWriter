public class Reader extends Thread {
    int myName;
    RandomSleep rSleep;
    
    public Reader(int name) {
        myName = name;
        rSleep = new RandomSleep();
    }
   
    public void run () {
        for (int I = 0;  I < 5; I++) {
            System.out.println("Reader " + myName + " wants to read." + Synch.readersWaiting);
            // Acquire "mutex" to start accessing shared counter variables
            // If other threads are using the mutex, wait until the thread releases "mutex"
            try{
                Synch.mutex.acquire();	// Acquire "mutex", permission to access shared variables
            }
            catch(Exception e){}
            // Checking current writer status
            // If writers are active, the reader will wait for the read to execute
            if (Synch.writersActive > 0 || Synch.writersWaiting > 0) {
            	Synch.readersWaiting++;		// Add a waiting reader
            	Synch.mutex.release();		// Releasing the "mutex"
            	try {
					Synch.ReaderSem.acquire();	// Getting permission to read
				} 
            	catch(Exception e){}
            	System.out.println("Reader " + myName + " is now reading.");
            }
            // No active writers, add an active read and continue reading data
            else {
            	Synch.readersActive++;		// Add current reader
            	System.out.println("Reader " + myName + " is now reading.");
            	Synch.mutex.release();		// Release the "mutex"
            }
            
            // Simulate the time taken for reading
            rSleep.doSleep(1, 200);
            
            // Finished reading
            // Acquire "mutex" to start accessing shared counter variables
            // If other threads are using the mutex, wait until the thread releases "mutex"
            try{
                Synch.mutex.acquire();	// Acquire "mutex", permission to access shared variables
            }
            catch(Exception e){}
            Synch.readersActive--;	// Finished reading on thread, remove reader
            // Checking for active reads and waiting writer threads
            // Give permission to writers if there are no more active readers
            if (Synch.readersActive == 0 && Synch.writersWaiting > 0) {
            	Synch.WriterSem.release();	// Allowing access to writer
            	Synch.writersWaiting--;		// Remove a waiting writer
            	Synch.writersActive++;		// Add an active writer
            }
            System.out.println("Reader " + myName + " is finished reading.");
            Synch.mutex.release();			// Release "mutex", permission for other threads to access shared variables
            // Simulate "doing something else".
            rSleep.doSleep(1, 1000);
        }
    }
}

