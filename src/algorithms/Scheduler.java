package algorithms;

import java.util.*;
import model.Process;
import model.Result;

public class Scheduler {

    private static void addGanttSegment(ArrayList<String> gantt, String pid, int start, int end) {
        if (start == end) return;

        if (!gantt.isEmpty()) {
            String last = gantt.get(gantt.size() - 1);
            String[] parts = last.split(",");

            String lastPid = parts[0];
            int lastStart = Integer.parseInt(parts[1]);
            int lastEnd = Integer.parseInt(parts[2]);

            if (lastPid.equals(pid) && lastEnd == start) {
                gantt.set(gantt.size() - 1, pid + "," + lastStart + "," + end);
                return;
            }
        }

        gantt.add(pid + "," + start + "," + end);
    }

    public static ScheduleOutput sjf(ArrayList<Process> processes) {
        ArrayList<Result> results = new ArrayList<>();
        ArrayList<String> gantt = new ArrayList<>();
        ArrayList<String> readyQueue = new ArrayList<>();

        boolean[] done = new boolean[processes.size()];
        int completed = 0;
        int time = 0;

        while (completed < processes.size()) {
            int index = -1;
            int minBurst = Integer.MAX_VALUE;
            ArrayList<String> queueItems = new ArrayList<>();

            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);

                if (!done[i] && p.arrival <= time) {
                    queueItems.add(p.pid + "(BT=" + p.burst + ")");

                    if (p.burst < minBurst) {
                        minBurst = p.burst;
                        index = i;
                    } else if (p.burst == minBurst && index != -1) {
                        if (p.arrival < processes.get(index).arrival) {
                            index = i;
                        }
                    }
                }
            }

            if (!queueItems.isEmpty()) {
                readyQueue.add("Time " + time + " -> [" + String.join(", ", queueItems) + "]");
            }

            if (index == -1) {
                int idleStart = time;
                time++;
                addGanttSegment(gantt, "IDLE", idleStart, time);
                continue;
            }

            Process p = processes.get(index);

            int start = time;
            int completion = time + p.burst;
            int turnaround = completion - p.arrival;
            int waiting = turnaround - p.burst;
            int response = start - p.arrival;

            addGanttSegment(gantt, p.pid, start, completion);

            results.add(new Result(
                    p.pid, p.arrival, p.burst,
                    completion, turnaround, waiting, response
            ));

            time = completion;
            done[index] = true;
            completed++;
        }

        return new ScheduleOutput(results, gantt, readyQueue);
    }

    public static ScheduleOutput srtf(ArrayList<Process> processes) {
        ArrayList<Result> results = new ArrayList<>();
        ArrayList<String> gantt = new ArrayList<>();
        ArrayList<String> readyQueue = new ArrayList<>();

        int n = processes.size();
        int[] remaining = new int[n];
        int[] completion = new int[n];
        int[] response = new int[n];
        boolean[] started = new boolean[n];

        Arrays.fill(response, -1);

        for (int i = 0; i < n; i++) {
            remaining[i] = processes.get(i).burst;
        }

        int time = 0;
        int completed = 0;

        String currentSegmentPid = "";
        int segmentStart = 0;
        String lastQueueText = "";

        while (completed < n) {
            int index = -1;
            int minRemaining = Integer.MAX_VALUE;
            ArrayList<String> queueItems = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);

                if (p.arrival <= time && remaining[i] > 0) {
                    queueItems.add(p.pid + "(RT=" + remaining[i] + ")");

                    if (remaining[i] < minRemaining) {
                        minRemaining = remaining[i];
                        index = i;
                    } else if (remaining[i] == minRemaining && index != -1) {
                        if (p.arrival < processes.get(index).arrival) {
                            index = i;
                        }
                    }
                }
            }

            String queueText = "Time " + time + " -> [" + String.join(", ", queueItems) + "]";
            if (!queueText.equals(lastQueueText)) {
                readyQueue.add(queueText);
                lastQueueText = queueText;
            }

            String selectedPid = (index == -1) ? "IDLE" : processes.get(index).pid;

            if (!selectedPid.equals(currentSegmentPid)) {
                if (!currentSegmentPid.equals("")) {
                    addGanttSegment(gantt, currentSegmentPid, segmentStart, time);
                }

                currentSegmentPid = selectedPid;
                segmentStart = time;
            }

            if (index == -1) {
                time++;
                continue;
            }

            Process current = processes.get(index);

            if (!started[index]) {
                response[index] = time - current.arrival;
                started[index] = true;
            }

            remaining[index]--;
            time++;

            if (remaining[index] == 0) {
                completion[index] = time;
                completed++;
            }
        }

        if (!currentSegmentPid.equals("")) {
            addGanttSegment(gantt, currentSegmentPid, segmentStart, time);
        }

        for (int i = 0; i < n; i++) {
            Process p = processes.get(i);

            int turnaround = completion[i] - p.arrival;
            int waiting = turnaround - p.burst;

            results.add(new Result(
                    p.pid, p.arrival, p.burst,
                    completion[i], turnaround, waiting, response[i]
            ));
        }

        return new ScheduleOutput(results, gantt, readyQueue);
    }

    public static ScheduleOutput roundRobin(ArrayList<Process> processes, int quantum) {
        ArrayList<Result> results = new ArrayList<>();
        ArrayList<String> gantt = new ArrayList<>();
        ArrayList<String> readyQueue = new ArrayList<>();

        int n = processes.size();
        int[] remaining = new int[n];
        int[] completion = new int[n];
        int[] response = new int[n];
        boolean[] started = new boolean[n];

        Arrays.fill(response, -1);

        for (int i = 0; i < n; i++) {
            remaining[i] = processes.get(i).burst;
        }

        Queue<Integer> queue = new LinkedList<>();
        boolean[] added = new boolean[n];

        int time = 0;
        int completed = 0;

        while (completed < n) {

            for (int i = 0; i < n; i++) {
                if (!added[i] && processes.get(i).arrival <= time) {
                    queue.add(i);
                    added[i] = true;
                }
            }

            ArrayList<String> queueItems = new ArrayList<>();
            for (int index : queue) {
                queueItems.add(processes.get(index).pid + "(RT=" + remaining[index] + ")");
            }

            if (!queueItems.isEmpty()) {
                readyQueue.add("Time " + time + " -> [" + String.join(", ", queueItems) + "]");
            }

            if (queue.isEmpty()) {
                int idleStart = time;
                time++;
                addGanttSegment(gantt, "IDLE", idleStart, time);
                continue;
            }

            int index = queue.poll();
            Process p = processes.get(index);

            if (!started[index]) {
                response[index] = time - p.arrival;
                started[index] = true;
            }

            int start = time;
            int runTime = Math.min(quantum, remaining[index]);

            time += runTime;
            remaining[index] -= runTime;

            addGanttSegment(gantt, p.pid, start, time);

            for (int i = 0; i < n; i++) {
                if (!added[i] && processes.get(i).arrival <= time) {
                    queue.add(i);
                    added[i] = true;
                }
            }

            if (remaining[index] > 0) {
                queue.add(index);
            } else {
                completion[index] = time;
                completed++;
            }
        }

        for (int i = 0; i < n; i++) {
            Process p = processes.get(i);

            int turnaround = completion[i] - p.arrival;
            int waiting = turnaround - p.burst;

            results.add(new Result(
                    p.pid, p.arrival, p.burst,
                    completion[i], turnaround, waiting, response[i]
            ));
        }

        return new ScheduleOutput(results, gantt, readyQueue);
    }
}