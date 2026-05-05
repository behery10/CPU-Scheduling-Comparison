package model;

public class Process {
    public String pid;
    public int arrival;
    public int burst;

    public Process(String pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
    }
}