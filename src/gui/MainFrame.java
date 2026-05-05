package gui;

import algorithms.ScheduleOutput;
import algorithms.Scheduler;
import model.Process;
import model.Result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    ArrayList<Process> processes = new ArrayList<>();

    DefaultTableModel processModel, rrModel, sjfModel, srtfModel, comparisonModel;

    JTextField pidField, arrivalField, burstField, quantumField;

    JPanel rrGanttPanel, sjfGanttPanel, srtfGanttPanel;

    JTextArea readyQueueArea, conclusionArea;

    public MainFrame() {

        setTitle("CPU Scheduling Comparison");
        setSize(1450, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("CPU Scheduling Comparison: RR vs SJF vs SRTF");
        title.setBounds(520, 10, 450, 30);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title);

        add(new JLabel("PID:")).setBounds(35, 55, 50, 25);
        pidField = new JTextField();
        pidField.setBounds(80, 55, 100, 25);
        add(pidField);

        add(new JLabel("Arrival:")).setBounds(200, 55, 60, 25);
        arrivalField = new JTextField();
        arrivalField.setBounds(260, 55, 100, 25);
        add(arrivalField);

        add(new JLabel("Burst:")).setBounds(380, 55, 50, 25);
        burstField = new JTextField();
        burstField.setBounds(430, 55, 100, 25);
        add(burstField);

        add(new JLabel("Quantum:")).setBounds(550, 55, 70, 25);
        quantumField = new JTextField();
        quantumField.setBounds(630, 55, 100, 25);
        add(quantumField);

        JButton addBtn = new JButton("Add Process");
        addBtn.setBounds(760, 55, 130, 25);
        add(addBtn);

        JButton runBtn = new JButton("Run");
        runBtn.setBounds(910, 55, 100, 25);
        add(runBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(1030, 55, 100, 25);
        add(clearBtn);

        processModel = new DefaultTableModel(new String[]{"PID", "Arrival", "Burst"}, 0);
        JTable processTable = new JTable(processModel);
        JScrollPane processScroll = new JScrollPane(processTable);
        processScroll.setBounds(35, 105, 400, 150);
        add(processScroll);

        readyQueueArea = new JTextArea("Run simulation to see ready queue...");
        readyQueueArea.setEditable(false);
        JScrollPane readyScroll = new JScrollPane(readyQueueArea);
        readyScroll.setBounds(460, 105, 920, 150);
        add(readyScroll);

        String[] cols = {"PID", "AT", "BT", "CT", "TAT", "WT", "RT"};

        add(new JLabel("Round Robin Results")).setBounds(35, 270, 200, 25);
        rrModel = new DefaultTableModel(cols, 0);
        JTable rrTable = new JTable(rrModel);
        JScrollPane rrScroll = new JScrollPane(rrTable);
        rrScroll.setBounds(35, 295, 400, 150);
        add(rrScroll);

        add(new JLabel("SJF Non-Preemptive Results")).setBounds(485, 270, 250, 25);
        sjfModel = new DefaultTableModel(cols, 0);
        JTable sjfTable = new JTable(sjfModel);
        JScrollPane sjfScroll = new JScrollPane(sjfTable);
        sjfScroll.setBounds(485, 295, 400, 150);
        add(sjfScroll);

        add(new JLabel("SRTF Preemptive SJF Results")).setBounds(935, 270, 250, 25);
        srtfModel = new DefaultTableModel(cols, 0);
        JTable srtfTable = new JTable(srtfModel);
        JScrollPane srtfScroll = new JScrollPane(srtfTable);
        srtfScroll.setBounds(935, 295, 400, 150);
        add(srtfScroll);

        add(new JLabel("Round Robin Gantt Chart")).setBounds(35, 455, 250, 25);
        rrGanttPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        rrGanttPanel.add(new JLabel("Run simulation to see chart"));
        JScrollPane rrGanttScroll = new JScrollPane(rrGanttPanel);
        rrGanttScroll.setBounds(35, 480, 1300, 70);
        add(rrGanttScroll);

        add(new JLabel("SJF Non-Preemptive Gantt Chart")).setBounds(35, 560, 300, 25);
        sjfGanttPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        sjfGanttPanel.add(new JLabel("Run simulation to see chart"));
        JScrollPane sjfGanttScroll = new JScrollPane(sjfGanttPanel);
        sjfGanttScroll.setBounds(35, 585, 1300, 70);
        add(sjfGanttScroll);

        add(new JLabel("SRTF Preemptive SJF Gantt Chart")).setBounds(35, 665, 300, 25);
        srtfGanttPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        srtfGanttPanel.add(new JLabel("Run simulation to see chart"));
        JScrollPane srtfGanttScroll = new JScrollPane(srtfGanttPanel);
        srtfGanttScroll.setBounds(35, 690, 1300, 70);
        add(srtfGanttScroll);

        comparisonModel = new DefaultTableModel(new String[]{"Metric", "Best Algorithm"}, 0);
        JTable comparisonTable = new JTable(comparisonModel);
        JScrollPane comparisonScroll = new JScrollPane(comparisonTable);
        comparisonScroll.setBounds(35, 770, 430, 90);
        add(comparisonScroll);

        conclusionArea = new JTextArea("Run simulation to see conclusion...");
        conclusionArea.setEditable(false);
        conclusionArea.setLineWrap(true);
        conclusionArea.setWrapStyleWord(true);
        JScrollPane conScroll = new JScrollPane(conclusionArea);
        conScroll.setBounds(485, 770, 850, 90);
        add(conScroll);

        addBtn.addActionListener(e -> addProcess());
        runBtn.addActionListener(e -> runSchedulers());
        clearBtn.addActionListener(e -> clearAll());

        setVisible(true);
    }

    private void addProcess() {
        String pid = pidField.getText().trim();

        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid Input: PID cannot be empty!");
            return;
        }

        int arrival, burst;

        try {
            arrival = Integer.parseInt(arrivalField.getText().trim());
            burst = Integer.parseInt(burstField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input: Arrival and Burst must be numbers!");
            return;
        }

        if (arrival < 0) {
            JOptionPane.showMessageDialog(this, "Invalid Input: Arrival time cannot be negative!");
            return;
        }

        if (burst <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid Input: Burst time must be greater than 0!");
            return;
        }

        for (Process oldProcess : processes) {
            if (oldProcess.pid.equalsIgnoreCase(pid)) {
                JOptionPane.showMessageDialog(this, "Invalid Input: Duplicate PID is not allowed!");
                return;
            }
        }

        processes.add(new Process(pid, arrival, burst));
        processModel.addRow(new Object[]{pid, arrival, burst});

        pidField.setText("");
        arrivalField.setText("");
        burstField.setText("");
    }

    private double[] fillTableAndGetAverages(DefaultTableModel model, ScheduleOutput output) {
        model.setRowCount(0);

        double totalWT = 0, totalTAT = 0, totalRT = 0;

        for (Result r : output.results) {
            model.addRow(new Object[]{
                    r.pid, r.arrival, r.burst,
                    r.completion, r.turnaround,
                    r.waiting, r.response
            });

            totalWT += r.waiting;
            totalTAT += r.turnaround;
            totalRT += r.response;
        }

        int n = output.results.size();

        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
        double avgRT = totalRT / n;

        model.addRow(new Object[]{
                "AVG", "-", "-", "-",
                String.format("%.2f", avgTAT),
                String.format("%.2f", avgWT),
                String.format("%.2f", avgRT)
        });

        return new double[]{avgWT, avgTAT, avgRT};
    }

    private void drawGantt(JPanel panel, ArrayList<String> ganttData) {
        panel.removeAll();

        for (String item : ganttData) {
            String[] parts = item.split(",");

            String pid = parts[0];
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);

            int duration = end - start;

            JLabel block = new JLabel(
                    "<html><center>" + pid + "<br>" + start + "-" + end + "</center></html>",
                    SwingConstants.CENTER
            );

            block.setOpaque(true);
            block.setBackground(Color.WHITE);
            block.setForeground(Color.BLACK);
            block.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            block.setFont(new Font("Arial", Font.BOLD, 13));

            int width = Math.max(65, duration * 45);
            block.setPreferredSize(new Dimension(width, 45));

            panel.add(block);
        }

        panel.revalidate();
        panel.repaint();
    }

    private void runSchedulers() {
        int quantum;

        try {
            quantum = Integer.parseInt(quantumField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input: Quantum must be a number!");
            return;
        }

        if (quantum <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid Input: Quantum must be greater than 0!");
            return;
        }

        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add processes first!");
            return;
        }

        ScheduleOutput rr = Scheduler.roundRobin(processes, quantum);
        ScheduleOutput sjf = Scheduler.sjf(processes);
        ScheduleOutput srtf = Scheduler.srtf(processes);

        double[] rrAvg = fillTableAndGetAverages(rrModel, rr);
        double[] sjfAvg = fillTableAndGetAverages(sjfModel, sjf);
        double[] srtfAvg = fillTableAndGetAverages(srtfModel, srtf);

        drawGantt(rrGanttPanel, rr.gantt);
        drawGantt(sjfGanttPanel, sjf.gantt);
        drawGantt(srtfGanttPanel, srtf.gantt);

        readyQueueArea.setText("Round Robin Ready Queue:\n");
        for (String s : rr.readyQueue) readyQueueArea.append(s + "\n");

        readyQueueArea.append("\nSJF Non-Preemptive Ready Queue:\n");
        for (String s : sjf.readyQueue) readyQueueArea.append(s + "\n");

        readyQueueArea.append("\nSRTF Preemptive SJF Ready Queue:\n");
        for (String s : srtf.readyQueue) readyQueueArea.append(s + "\n");

        comparisonModel.setRowCount(0);
        comparisonModel.addRow(new Object[]{"Best Waiting Time", bestMetric(rrAvg[0], sjfAvg[0], srtfAvg[0])});
        comparisonModel.addRow(new Object[]{"Best Turnaround Time", bestMetric(rrAvg[1], sjfAvg[1], srtfAvg[1])});
        comparisonModel.addRow(new Object[]{"Best Response Time", bestMetric(rrAvg[2], sjfAvg[2], srtfAvg[2])});
        comparisonModel.addRow(new Object[]{"Recommended", bestMetric(rrAvg[0], sjfAvg[0], srtfAvg[0])});

        conclusionArea.setText(buildConclusion(rrAvg, sjfAvg, srtfAvg, quantum));
    }

    private String buildConclusion(double[] rrAvg, double[] sjfAvg, double[] srtfAvg, int quantum) {
        StringBuilder sb = new StringBuilder();

        String bestWT = bestMetric(rrAvg[0], sjfAvg[0], srtfAvg[0]);
        String bestTAT = bestMetric(rrAvg[1], sjfAvg[1], srtfAvg[1]);
        String bestRT = bestMetric(rrAvg[2], sjfAvg[2], srtfAvg[2]);

        sb.append("Efficiency: ").append(bestWT)
                .append(String.format(" achieved the lowest average waiting time (%.2f).\n",
                        Math.min(rrAvg[0], Math.min(sjfAvg[0], srtfAvg[0]))));

        sb.append("Turnaround: ").append(bestTAT)
                .append(String.format(" had the lowest average turnaround time (%.2f).\n",
                        Math.min(rrAvg[1], Math.min(sjfAvg[1], srtfAvg[1]))));

        sb.append("Response Time: ").append(bestRT)
                .append(String.format(" had the lowest average response time (%.2f).\n",
                        Math.min(rrAvg[2], Math.min(sjfAvg[2], srtfAvg[2]))));

        if (rrAvg[2] <= sjfAvg[2] && rrAvg[2] <= srtfAvg[2]) {
            sb.append("Fairness: Round Robin appeared more responsive in this workload.\n");
        } else {
            sb.append("Fairness: Round Robin gives each process a turn, but it was not the best in response time here.\n");
        }

        if (sjfAvg[0] < rrAvg[0] || srtfAvg[0] < rrAvg[0]) {
            sb.append("Short Jobs: SJF/SRTF completed short jobs more efficiently than Round Robin.\n");
        } else {
            sb.append("Short Jobs: Round Robin performed close to or better than SJF/SRTF in this workload.\n");
        }

        if (quantum <= 2) {
            sb.append("Quantum Effect: Small quantum (").append(quantum)
                    .append(") increases context switching overhead.\n");
        } else if (quantum >= 8) {
            sb.append("Quantum Effect: Large quantum (").append(quantum)
                    .append(") makes RR closer to FCFS.\n");
        } else {
            sb.append("Quantum Effect: Quantum (").append(quantum)
                    .append(") gives a balance between fairness and overhead.\n");
        }

        sb.append("Recommendation: Use ").append(bestWT)
                .append(" for efficiency, and Round Robin when fairness is the priority.");

        return sb.toString();
    }

    private String bestMetric(double rr, double sjf, double srtf) {
        double min = Math.min(rr, Math.min(sjf, srtf));

        if (min == rr && min == sjf && min == srtf) {
            return "All algorithms are equal";
        } else if (min == rr) {
            return "Round Robin";
        } else if (min == sjf) {
            return "SJF Non-Preemptive";
        } else {
            return "SRTF Preemptive SJF";
        }
    }

    private void clearAll() {
        processes.clear();

        processModel.setRowCount(0);
        rrModel.setRowCount(0);
        sjfModel.setRowCount(0);
        srtfModel.setRowCount(0);
        comparisonModel.setRowCount(0);

        rrGanttPanel.removeAll();
        sjfGanttPanel.removeAll();
        srtfGanttPanel.removeAll();

        rrGanttPanel.add(new JLabel("Run simulation to see chart"));
        sjfGanttPanel.add(new JLabel("Run simulation to see chart"));
        srtfGanttPanel.add(new JLabel("Run simulation to see chart"));

        rrGanttPanel.revalidate();
        sjfGanttPanel.revalidate();
        srtfGanttPanel.revalidate();

        rrGanttPanel.repaint();
        sjfGanttPanel.repaint();
        srtfGanttPanel.repaint();

        readyQueueArea.setText("Run simulation to see ready queue...");
        conclusionArea.setText("Run simulation to see conclusion...");

        quantumField.setText("");
        pidField.setText("");
        arrivalField.setText("");
        burstField.setText("");
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}