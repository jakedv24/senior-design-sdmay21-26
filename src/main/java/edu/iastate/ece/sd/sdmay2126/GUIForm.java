package edu.iastate.ece.sd.sdmay2126;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIForm extends JFrame{
    private JButton runDefaultSettingsButton;
    private JCheckBox bool1CheckBox;
    private JCheckBox bool3CheckBox;
    private JCheckBox bool2CheckBox;
    private JCheckBox bool4CheckBox;
    public JPanel MainPanel;
    public boolean RunDefault = false;

    public GUIForm(){
        add(MainPanel); //Display Panel
        setSize(500,500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        runDefaultSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RunDefault = !RunDefault; //Flip the switch to signal to main to close the GUI

                //Close the Jpanel and free the resources it used.
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
            }
        });
    }
}
