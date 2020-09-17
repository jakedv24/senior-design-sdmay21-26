package edu.iastate.ece.sd.sdmay2126;

import org.openqa.selenium.WebDriver;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WebDriver driver = App.getDriver();
                        try {
                            System.out.println("Loading KBase narrative...");
                            driver.get("https://narrative.kbase.us/narrative/71238");

                            // Let things load
                            Thread.sleep(3000);

                            // Login with Globus
                            App.performGlobusAuthFlow(driver);

                            // This load really takes a while
                            Thread.sleep(15000);

                            // TODO: Interact with the narrative (fill form, click buttons, read output)

                            // Wait so we can observe/mess around
                            System.out.println("Waiting 5 minutes so we can experiment");
                            Thread.sleep(5 * 60 * 1000);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        } finally {
                            System.out.println("Run done - closing driver and quitting.");
                            driver.close();
                        }
                    }
                }).start();
            }
        });
    }
}
