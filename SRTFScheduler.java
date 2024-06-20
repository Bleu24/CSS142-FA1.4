import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class SRTFScheduler {

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 6));
        processes.add(new Process(2, 1, 3));
        processes.add(new Process(3, 2, 8));
        processes.add(new Process(4, 3, 4));
        processes.add(new Process(5, 4, 2));
        
        SRTFScheduler scheduler = new SRTFScheduler();
        scheduler.schedule(processes);
    }
    
    public void schedule(List<Process> processes) {
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> p1.remainingTime - p2.remainingTime);
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        
        while (completed != n) {
            // Add all processes that have arrived by the current time to the ready queue
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
            
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                currentProcess.remainingTime--;
                currentTime++;
                
                if (currentProcess.remainingTime == 0) {
                    currentProcess.completionTime = currentTime;
                    completed++;
                    
                    turnaroundTime[currentProcess.processId - 1] = currentProcess.completionTime - currentProcess.arrivalTime;
                    waitingTime[currentProcess.processId - 1] = turnaroundTime[currentProcess.processId - 1] - currentProcess.burstTime;
                } else {
                    readyQueue.add(currentProcess); // Re-add the process with updated remaining time
                }
            } else {
                currentTime++; // If no process is ready, increment the time
            }
        }
        
        // Calculate and print average waiting time and turnaround time
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        
        for (int i = 0; i < n; i++) {
            totalWaitingTime += waitingTime[i];
            totalTurnaroundTime += turnaroundTime[i];
        }
        
        System.out.println("Average Waiting Time: " + (totalWaitingTime / n));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / n));
    }
}
