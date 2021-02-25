package edu.iastate.ece.sd.sdmay2126.gui.status;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class JobStatusTable extends JTable {
    private final DefaultTableModel model;

    public JobStatusTable() {
        model = new DefaultTableModel();
        model.addColumn("Job");
        model.addColumn("Status");
        setModel(model);

        getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
    }

    public void updateRowStatus(int rowIndex, String newStatus) {
        if (rowIndex >= model.getRowCount()) {
            model.addRow(new String[] { "Job " + (rowIndex+1), newStatus });
        } else {
            model.setValueAt(newStatus, rowIndex, 1);
        }
    }
}
