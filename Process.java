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
