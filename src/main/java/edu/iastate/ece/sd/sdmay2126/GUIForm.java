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

    //GUI Components
    private JButton runDefaultSettingsButton;
    private JCheckBox fluxVariabilityAnalysis; //boolean, check is 1 unchecked is 0
    private JCheckBox simulateAllSingleKos; //boolean, check is 1 unchecked is 0
    private JCheckBox minimizeFlux; //boolean, check is 1 unchecked is 0
    private JTextField ErrorTextField;

    //Float values GUI fields
    private JTextField activationCoefficientText;
    private JTextField CarbonUptake;
    private JTextField NitrogenUptake;
    private JTextField PhosphateUptake;
    private JTextField sulfurUptake;
    private JTextField oxygenUptake;
    private JTextField reactionToMaximize;
    private JTextField expressionThreshold;
    private JTextField expressionUncertainty;

    //Strings for floats
    public String activationCoefficientString; //the string to be converted
    public String CarbonString;
    public String NitrogenString;
    public String PhosphateString;
    public String SulfurString;
    public String OxygenString;
    public String ExpressionThreshold;
    public String ExpressionUncertainty;

    public JPanel MainPanel;

    //GUI Form variables to send to driver. Some have defaults set here, some do not.
    public boolean fluxVariabilityAnalysisValue = true; //Value read from the checkbox. Default = 1
    public boolean simulateAllSingleKosValue = true; //Value read from the checkbox. Default = 1
    public boolean minimizeFluxValue = true; //Value read from the checkbox. Default = 1
    public float activationCoefficient = (float) 0.5; //the float that stores the coefficient. default is 0.5
    public float CarbonValue; //no default
    public float NitrogenValue; //no default
    public float PhosphateValue; //no default
    public float SulfurValue; //no default
    public float OxygenValue; //no default
    public float ExpressionThresholdValue = (float) 0.5;
    public float ExpressionUncertaintyValue = (float) 0.1;

    private boolean formError = false; //try catches will signal this.

    public GUIForm(JobManager jobManager) {
        this.jobManager = jobManager;

        //Set checkboxes to true as this is the default
        fluxVariabilityAnalysis.setSelected(true);
        simulateAllSingleKos.setSelected(true);
        minimizeFlux.setSelected(true);

        add(MainPanel); //Display Panel
        setSize(500, 500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        activationCoefficientText.setText("Activation Coefficient [0,1]");

        //Give all the text that "hint" look in the GUI
        activationCoefficientText.setForeground(Color.gray);
        CarbonUptake.setForeground(Color.gray);
        NitrogenUptake.setForeground(Color.gray);
        PhosphateUptake.setForeground(Color.gray);
        sulfurUptake.setForeground(Color.gray);
        oxygenUptake.setForeground(Color.gray);
        reactionToMaximize.setForeground(Color.gray);
        expressionThreshold.setForeground(Color.gray);
        expressionUncertainty.setForeground(Color.gray);

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
        CarbonUptake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (CarbonUptake.getText().equals("*Carbon Uptake [0,100]")) {
                    CarbonUptake.setText("");
                    CarbonUptake.setForeground(Color.BLACK);
                }
                CarbonString = CarbonUptake.getText();
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
                CarbonString = CarbonUptake.getText(); //save the Carbon coefficient at run time.
                try {
                    //Check if the user left the value as a default value
                    if(activationCoefficientString.equals("") || activationCoefficientString.equals("Activation Coefficient [0,1]")){
                        activationCoefficient = (float) 0.5;
                    }
                    else if(!activationCoefficientString.equals("Activation Coefficient [0,1]")) {
                        activationCoefficient = Float.parseFloat(activationCoefficientString); //if not default set as user value
                    }

                } catch (NumberFormatException k) {
                    FloatException("Activation Coefficient", 0, 1);
                }
                validationRange(0.0,1.0,activationCoefficient, "Activation Coefficient");

                //Setting the Carbon string from the GUI for the web driver
                try {
                    //Check if the user left the value as a default value
                    if(CarbonString.equals("*Carbon Uptake [0,100]") || CarbonString.equals("")){ //this value can't be left blank, no specified default
                        formError = true;
                        ErrorTextField.setText("Carbon Uptake Field is required. Range: 0-100");
                    }
                    else{
                        CarbonValue = Float.parseFloat(CarbonString); //if not default set as user value
                    }

                } catch (NumberFormatException k) {
                    FloatException("Carbon Uptake", 0, 100);
                }
                validationRange(0.0,100.0, CarbonValue, "Carbon Uptake");

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
                        params.setActivationCoefficient(activationCoefficient);

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
        ErrorTextField.setText(valueField + " must be an integer between " +  min + " - " + max + " inclusive");
        formError = true;
    }
}
