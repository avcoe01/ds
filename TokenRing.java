import java.util.concurrent.locks.*;

public class TokenRing {
    private static final int NUM_PROCESSES = 5;  // Number of processes in the ring
    private static final int CRITICAL_SECTION_DELAY = 1000;  // Simulate critical section delay

    private static Process[] processes = new Process[NUM_PROCESSES];
    private static int tokenHolder = 0;  // Process holding the token

    public static void main(String[] args) {
        // Initialize processes
        for (int i = 0; i < NUM_PROCESSES; i++) {
            processes[i] = new Process(i);
        }

        // Start processes
        for (int i = 0; i < NUM_PROCESSES; i++) {
            new Thread(processes[i]).start();
        }
    }

    // Process Class represents each node in the Token Ring
    static class Process implements Runnable {
        private int processId;  // Process ID

        public Process(int processId) {
            this.processId = processId;
        }

        @Override
        public void run() {
            while (true) {
                if (processId == tokenHolder) {
                    enterCriticalSection();
                    passToken();
                }
                try {
                    Thread.sleep(500);  // Simulate waiting time before checking token
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void enterCriticalSection() {
            System.out.println("Process " + processId + " entering critical section.");
            try {
                // Simulate some work in the critical section
                Thread.sleep(CRITICAL_SECTION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Process " + processId + " leaving critical section.");
        }

        private synchronized void passToken() {
            tokenHolder = (tokenHolder + 1) % NUM_PROCESSES;  // Pass the token to the next process
            System.out.println("Process " + processId + " passed the token to Process " + tokenHolder);
        }
    }
}