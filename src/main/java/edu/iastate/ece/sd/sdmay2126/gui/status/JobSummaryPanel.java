package edu.iastate.ece.sd.sdmay2126.gui.status;

import javax.swing.*;

public class JobSummaryPanel extends JPanel {
    private final JLabel summaryLabel;

    public JobSummaryPanel() {
        summaryLabel = new JLabel("Waiting for jobs...");
        add(summaryLabel);
    }

    public void updateSummaryText(String updatedText) {
        summaryLabel.setText(updatedText);
    }
}
