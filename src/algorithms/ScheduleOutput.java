package algorithms;

import java.util.ArrayList;
import model.Result;

public class ScheduleOutput {
    public ArrayList<Result> results;
    public ArrayList<String> gantt;
    public ArrayList<String> readyQueue;

    public ScheduleOutput(ArrayList<Result> results,
                          ArrayList<String> gantt,
                          ArrayList<String> readyQueue) {
        this.results = results;
        this.gantt = gantt;
        this.readyQueue = readyQueue;
    }
}