import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

class SRTFSchedulerRandomized {

    public static void main(String[] args) {
        List<Process> processes = generateProcesses(); // Generate processes with random arrival times and fixed burst times
        
        SRTFScheduler scheduler = new SRTFScheduler();
        scheduler.schedule(processes);
    }
    
    public static List<Process> generateProcesses() {
        List<Process> processes = new ArrayList<>();
        Random random = new Random();

        // Define fixed burst times
        int[] burstTimes = {3, 5, 6, 5, 4};

        for (int i = 1; i <= burstTimes.length; i++) {
            int arrivalTime = random.nextInt(10); // Random arrival time between 0 and 9
            int burstTime = burstTimes[i - 1]; // Fixed burst time
            processes.add(new Process(i, arrivalTime, burstTime));
        }
        
        // Print the generated processes for reference
        System.out.println("Generated Processes:");
        for (Process p : processes) {
            System.out.println("Process " + p.processId + ": Arrival Time = " + p.arrivalTime + ", Burst Time = " + p.burstTime);
        }
        
        return processes;
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