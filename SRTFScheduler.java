import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class Process {
    int processId;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;

    public Process(int processId, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
    }
}

class SRTFScheduler {

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 3));
        processes.add(new Process(2, 2, 5));
        processes.add(new Process(3, 6, 6));
        processes.add(new Process(4, 8, 5));
        processes.add(new Process(5, 3, 4));
        
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
        List<String> ganttChart = new ArrayList<>();
        
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
                ganttChart.add("Time " + currentTime + ": P" + currentProcess.processId + " (Remaining Time: " + currentProcess.remainingTime + ")");
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
                ganttChart.add("Time " + currentTime + ": Idle");
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
        
        // Print Gantt chart
        System.out.println("Gantt Chart:");
        for (String entry : ganttChart) {
            System.out.println(entry);
        }
    }
}
