public class Writer extends Thread {
    int myName;

    RandomSleep rSleep;
    
    public Writer(int name) {
        myName = name;
        rSleep = new RandomSleep();
    }
   
    public void run () {
        for (int I = 0;  I < 5; I++) {  
            // Acquire "mutex" to start accessing shared counter variables and start writing
            // If other threads are using the mutex, wait until the thread releases "mutex"
            System.out.println("Writer " + myName + " wants to write." + Synch.writersWaiting);
            try{
                Synch.mutex.acquire();	// Acquire "mutex", permission to access shared variables
            }
            catch(Exception e){}
            // Check if any readers or other writers are active, wait if there are active readers/writers
            if (Synch.readersActive > 0 || Synch.writersActive > 0) {
            	Synch.writersWaiting++;		// Add a waiting writer
            	Synch.mutex.release();		// Releasing the "mutex" to other threads
            	try {
					Synch.WriterSem.acquire();	// Wait for permission to start writing
				} 
            	catch(Exception e){}
            	System.out.println("Writer " + myName + " is now writing");
            }
            // No active readers or writers writers, add an active writer and write data
            else {
            	Synch.writersActive++;	// Add an active writer
            	System.out.println("Writer " + myName + " is now writing");
            	Synch.mutex.release();	// Release the "mutex"
            }
            // Simulate the time taken by writing.
            rSleep.doSleep(1, 200);
            
            // Finish writing
            // Acquire "mutex" to start accessing shared counter variables and start writing
            // If other threads are using the mutex, wait until the thread releases "mutex"
            try {
				Synch.mutex.acquire();
			} 
            catch(Exception e){}
            Synch.writersActive--; // Remove an active writer
            // Check if any reader threads are waiting
            if (Synch.readersWaiting > 0) {
            	// Release all waiting readers to reader threads to start reading
            	for (int i = 1; i <= Synch.readersWaiting; i++) {
            		Synch.ReaderSem.release();	// Release reader semaphore
            	}
            	// Reset shared counter variables
            	Synch.readersActive = Synch.readersWaiting;	// Set all waiting readers to active
            	Synch.readersWaiting = 0;					// Reset waiting readers, all waiting reads are active
            }
            // Check if any writing threads are waiting
            else if (Synch.writersWaiting > 0) {
            	Synch.WriterSem.release();	// Release writer semaphore, allow another writer thread to write
            	Synch.writersWaiting--;		// Remove one writer from waiting
            	Synch.writersActive++;		// Add one writer to active
            }
            System.out.println("Writer " + myName + " is finished writing");
            Synch.mutex.release();			// Release the "mutex", allow other threads to access shared variables
            
            // Simulate "doing something else"
            rSleep.doSleep(1, 1000);
        }
    }
}

