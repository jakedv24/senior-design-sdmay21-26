package edu.iastate.ece.sd.sdmay2126.gui.status;

import javax.swing.*;
import java.awt.*;

public class JobStatusPanel extends JPanel {
    private final JobStatusTable statusTable;

    public JobStatusPanel() {
        statusTable = new JobStatusTable();

        JScrollPane scrollPane = new JScrollPane(statusTable);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        add(scrollPane);
    }

    public JobStatusTable getStatusTable() {
        return statusTable;
    }
}
