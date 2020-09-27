package edu.iastate.ece.sd.sdmay2126;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManagerStoppedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIForm extends JFrame {
    private final JobManager jobManager;

    private JButton runDefaultSettingsButton;
    private JCheckBox fluxVariabilityAnalysis; //boolean, check is 1 unchecked is 0
    private JCheckBox simulateAllSingleKos; //boolean, check is 1 unchecked is 0
    private JCheckBox minimizeFlux; //boolean, check is 1 unchecked is 0
    private JTextField activationCoefficientText; //Float, Range between 0-1
    public float activationCoefficient = (float) 0.5; //the float that stores the coefficient. default is 0.5
    public String activationCoefficientString; //the string to be converted
    public JPanel MainPanel;
    private JTextField ErrorTextField;
    private JTextField CarbonUptake;
    private JTextField NitrogenUptake;
    private JTextField PhosphateUptake;
    private JTextField sulfurUptake;
    private JTextField oxygenUptake;
    private JTextField reactionToMaximize;
    private JTextField expressionThreshold;
    private JTextField expressionUncertainty;
    public boolean fluxVariabilityAnalysisValue = true; //Value read from the checkbox. Default = 1
    public boolean simulateAllSingleKosValue = true; //Value read from the checkbox. Default = 1
    public boolean minimizeFluxValue = true; //Value read from the checkbox. Default = 1
    private boolean formError = false; //try catches will signal this.

    public GUIForm(JobManager jobManager) {
        this.jobManager = jobManager;

        add(MainPanel); //Display Panel
        setSize(500, 500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        activationCoefficientText.setText("Activation Coefficient [0,1]");
        activationCoefficientText.setForeground(Color.gray);
        activationCoefficientText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (activationCoefficientText.getText().equals("Activation Coefficient [0,1]")) {
                    activationCoefficientText.setText("");
                    activationCoefficientText.setForeground(Color.BLACK);
                }
                activationCoefficientString = activationCoefficientText.getText();
            }
        });
        runDefaultSettingsButton.addActionListener(new ActionListener() {
            /*
            When the user presses the "run" button, We are going to save all the variables
            that they have input.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                formError = false;
                activationCoefficientString = activationCoefficientText.getText(); //Save the activation coefficient
                try {
                    //Check if the user left the value as a default value
                    if(!activationCoefficientString.equals("Activation Coefficient [0,1]")) {
                        activationCoefficient = Float.parseFloat(activationCoefficientString); //if not default set as user value
                    }

                } catch (NumberFormatException k) {
                    FloatException("Activation Coefficient", 0, 1);
                }
                validationRange(0.0,1.0,activationCoefficient, "Activation Coefficient");
                //Viewing the checklists of the 3 booleans and setting the values appropriately.
                fluxVariabilityAnalysisValue = fluxVariabilityAnalysis.isSelected();
                simulateAllSingleKosValue = simulateAllSingleKos.isSelected();
                minimizeFluxValue = minimizeFlux.isSelected();
                //Close the Jpanel and free the resources it used.
                if (!formError) {
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();

                    // Create and queue a job from the user's inputs
                    try {
                        // Setup the parameters
                        FBAParameters params = new FBAParameters(fluxVariabilityAnalysisValue, minimizeFluxValue, simulateAllSingleKosValue);
                        params.setActivationCoefficient(Float.parseFloat(activationCoefficientString));

                        // Queue the job
                        jobManager.scheduleJob(new Job(params));
                    } catch (JobManagerStoppedException jobManagerStoppedException) {
                        // TODO: Handle better
                        jobManagerStoppedException.printStackTrace();
                    }
                }
            }
        });

    }
    /*
    simple method to validate if the user enters a value that is within the range of values acceptable
     */
    private void validationRange(double min, double max, Float userValue, String valueField){
        if(!(userValue >= min && userValue <= max)){
            formError = true;
            FloatException(valueField, min, max);
        }
    }
    /*
    this code runs to validate if the float value is a integer value.
    if it is not an integer value, then the error message pops up and we stop the runner from executing until we have good values
     */
    private void FloatException(String valueField, double min, double max){
        ErrorTextField.setVisible(true);
        //TODO Make error message more noticeable.
        ErrorTextField.setForeground(Color.red);
        MainPanel.revalidate();
        MainPanel.repaint();
        ErrorTextField.setText(valueField + " must be an integer between " +  min + "-" + max + " inclusive");
        formError = true;
    }
}
