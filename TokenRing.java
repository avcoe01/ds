import java.util.*; 
import java.util.concurrent.*; 

class TokenRing { 
    static final int NUM_PROCESSES = 3; 
    static final int MAX_ITERATIONS = 1; // Number of times each process accesses critical section
    static List<Process> processes = new ArrayList<>(); 
    static Semaphore mutex = new Semaphore(1); 
    static int tokenHolder = 0; 
    static Random random = new Random(); 

    public static void main(String[] args) throws InterruptedException { 
        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < NUM_PROCESSES; i++) { 
            Process p = new Process(i); 
            processes.add(p); 
            Thread t = new Thread(p);
            threads.add(t);
            t.start(); 
        } 
        
        // Wait for all threads to complete
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("All processes have completed their execution.");
    } 

    static class Process implements Runnable { 
        int id; 

        Process(int id) { 
            this.id = id; 
        } 

        public void run() { 
            for (int i = 0; i < MAX_ITERATIONS; i++) { 
                try { 
                    Thread.sleep(random.nextInt(1000)); // Simulate random wait 
                    requestToken(); 
                    enterCriticalSection(i); 
                    releaseToken(); 
                    Thread.sleep(random.nextInt(1000)); 
                } catch (InterruptedException e) { 
                    e.printStackTrace(); 
                } 
            } 
            System.out.println("Process " + id + " has finished its operations.");
        } 

        void requestToken() throws InterruptedException { 
            mutex.acquire(); 
            while (tokenHolder != id) { 
                System.out.println("Process " + id + " waiting for the token."); 
                mutex.release(); 
                Thread.sleep(100); 
                mutex.acquire(); 
            } 
            System.out.println("Process " + id + " acquired the token."); 
            mutex.release(); 
        } 

        void enterCriticalSection(int iteration) throws InterruptedException { 
            System.out.println("Process " + id + " entering critical section. [Iteration: " + (iteration + 1) + "]"); 
            Thread.sleep(random.nextInt(500)); 
        } 

        void releaseToken() throws InterruptedException { 
            mutex.acquire(); 
            System.out.println("Process " + id + " exiting critical section and passing token."); 
            tokenHolder = (tokenHolder + 1) % NUM_PROCESSES; 
            mutex.release(); 
        } 
    } 
}
