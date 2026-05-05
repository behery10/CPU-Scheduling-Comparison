# CPU Scheduling Comparison Project

Project Overview:

This project was developed for the Operating Systems course to compare CPU scheduling algorithms using a Java GUI application.

The system allows the user to enter processes dynamically, run multiple scheduling algorithms, and compare their performance using common scheduling metrics.

Implemented Algorithms

The project implements and compares the following algorithms:

1. Round Robin (RR)
2. Shortest Job First (SJF - Non-Preemptive)
3. Shortest Remaining Time First (SRTF - Preemptive SJF)

Project Features

- Dynamic process input using PID, Arrival Time, and Burst Time
- Time Quantum input for Round Robin
- Input validation for invalid values and duplicate process IDs
- Separate result tables for each algorithm
- Visual Gantt Chart for each algorithm
- Ready Queue display
- Average Waiting Time, Turnaround Time, and Response Time
- Comparison summary showing the best algorithm for each metric
- Dynamic conclusion based on the actual results
- Idle CPU time handling in the Gantt Chart

 Scheduling Metrics

The project calculates:

- Completion Time (CT)
- Turnaround Time (TAT)
- Waiting Time (WT)
- Response Time (RT)

src/
├── algorithms/
│   ├── Scheduler.java
│   └── ScheduleOutput.java
├── gui/
│   └── MainFrame.java
└── model/
    ├── Process.java
    └── Result.java 

    
How to Run:
1-Open the project in a Java IDE such as NetBeans, IntelliJ IDEA, or Eclipse.
2-Open the file: src/gui/MainFrame.java
3-Run the MainFrame class.
4-Enter the process data and quantum value.
5-Click Run to display the scheduling results.

Example Test Case:
Quantum = 2
P1  Arrival = 3  Burst = 4
P2  Arrival = 5  Burst = 2
P3  Arrival = 6  Burst = 1
P4  Arrival = 7  Burst = 3

Conclusion

This project demonstrates the difference between fairness-based and efficiency-based CPU scheduling.

Round Robin provides fair CPU distribution by giving each process a turn, while SJF and SRTF often reduce waiting time and turnaround time by prioritizing shorter jobs.

Course Information

Course: Operating Systems
Project: Scheduling Comparison Project
Algorithms: Round Robin, SJF, SRTF
