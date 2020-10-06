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
    public float activationCoefficient; //the float that stores the coefficient
    public String activationCoefficientString; //the string to be converted
    private JTextField errorTextField;
    public boolean fluxVariabilityAnalysisValue = false; //Value read from the checkbox.
    public boolean simulateAllSingleKosValue = false; //Value read from the checkbox.
    public boolean minimizeFluxValue = false; //Value read from the checkbox.
    private boolean formError = false; //try catches will signal this.

    public JPanel mainPanel;
    private JTextField reactionToMaximize; //name of the reaction to Maximize
    private  String reactionToMaximizeString;

    //TODO (DL): Set error message to red, set error message if number is outside
    // of the range, set booleans to values, handoff to driver.
    //TODO (DL) Cont, comment new additions to code.
    public GUIForm(JobManager jobManager) {
        this.jobManager = jobManager;

        mainPanel.setLayout(new GridLayout(5, 6)); // Sln. for NPEs: https://stackoverflow.com/a/10603083

        add(mainPanel); //Display Panel
        setSize(500, 500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        activationCoefficientText.setText("Activation Coefficient [0,1]");
        activationCoefficientText.setForeground(Color.gray);
        errorTextField.setVisible(false);
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

        reactionToMaximize.setForeground(Color.gray);
        reactionToMaximize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

            super.mouseClicked(e);
            if(reactionToMaximize.getText().equals("Reaction To Maximize")) {
                reactionToMaximize.setText("");
                reactionToMaximize.setForeground((Color.black));
            }
            reactionToMaximizeString = reactionToMaximize.getText();
        }});

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
                    activationCoefficient = Float.parseFloat(activationCoefficientString);
                } catch (NumberFormatException k) {
                    errorTextField.setVisible(true);

                    errorTextField.setSelectionColor(Color.red);
                    errorTextField.setForeground(Color.red);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    errorTextField.setText("Activation Coefficient must be an integer between 0-1 inclusive");
                    formError = true;
                }
                reactionToMaximizeString = reactionToMaximize.getText(); //save name of reaction to maximize
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
                        FBAParameters params = new FBAParameters(fluxVariabilityAnalysisValue,
                                minimizeFluxValue, simulateAllSingleKosValue);
                        params.setActivationCoefficient(Float.parseFloat(activationCoefficientString));
                        params.setReactionToMaximize(reactionToMaximizeString);

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
}
