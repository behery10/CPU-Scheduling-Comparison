package model;

public class Result {
    public String pid;
    public int arrival, burst, completion, turnaround, waiting, response;

    public Result(String pid, int arrival, int burst,
                  int completion, int turnaround, int waiting, int response) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.completion = completion;
        this.turnaround = turnaround;
        this.waiting = waiting;
        this.response = response;
    }
}