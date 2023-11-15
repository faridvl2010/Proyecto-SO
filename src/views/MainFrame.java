package views;


import models.MyProcess;
import models.Queue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private static final String TITLE = "Software 1";
    private ActionListener listener;
    private MainPanel mainPanel;

    public MainFrame(ActionListener listener) {
        this.listener = listener;
        setUndecorated(true);
        getContentPane().setLayout(new GridLayout(1,1));
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initProcessesPanel();
        setExtendedState(MAXIMIZED_BOTH);
    }

    public void initProcessesPanel(){
        mainPanel = new MainPanel(listener);
        add(mainPanel);
    }

    public void updateProcesses(Queue<MyProcess> processQueue){
        mainPanel.updateProcesses(processQueue);
    }

    public void initReportsPanel(ArrayList<MyProcess> readyProcess, ArrayList<MyProcess> dispatchedProcess,
                                 ArrayList<MyProcess> executingProcess, ArrayList<MyProcess> toLockedProcess,
                                 ArrayList<MyProcess> lockedProcess, ArrayList<MyProcess> wakeUpProcess,
                                 ArrayList<MyProcess> expiredProcess, ArrayList<MyProcess> terminatedProcess){
        mainPanel.initReportsPanel(readyProcess, dispatchedProcess, executingProcess, toLockedProcess,
                                    lockedProcess, wakeUpProcess, expiredProcess, terminatedProcess);
    }

    public void newSimulation(){
        getContentPane().remove(mainPanel);
        mainPanel = new MainPanel(listener);
        add(mainPanel);
        getContentPane().revalidate();
    }

    public String getPdfPath() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBackground(Color.WHITE);
        fileChooser.showOpenDialog(this);
        return fileChooser.getSelectedFile().getPath();
    }
}
