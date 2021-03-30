package edu.iastate.ece.sd.sdmay2126.input.gui;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.input.file.FileInputReader;
import edu.iastate.ece.sd.sdmay2126.input.file.JSONFileInputReader;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import edu.iastate.ece.sd.sdmay2126.orchestration.JobManagerStoppedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import static edu.iastate.ece.sd.sdmay2126.util.RandomUtil.getRandBoolean;
import static edu.iastate.ece.sd.sdmay2126.util.RandomUtil.getRandInRange;

public class GUIForm extends JFrame {
    private final JobManager jobManager;
    //Strings for floats
    public String activationCoefficientString; //the string to be converted
    public String carbonString;
    public String nitrogenString;
    public String phosphateString;
    public String sulfurString;
    public String oxygenString;
    public String expressionthresholdString;
    public String expressionUncertaintyString;
    public String mediaSupplementString;
    public String reactionToMaximizeString;
    public String numberJobsString;
    public String geneKnockoutsString;
    public String expressionConditionString;

    public JPanel mainPanel;
    //GUI Form variables to send to driver. Some have defaults set here, some do not.
    public boolean fluxVariabilityAnalysisValue = true; //Value read from the checkbox. Default = 1
    public boolean simulateAllSingleKosValue = true; //Value read from the checkbox. Default = 1
    public boolean minimizeFluxValue = true; //Value read from the checkbox. Default = 1
    public float activationCoefficient = (float) 0.5; //the float that stores the coefficient. default is 0.5
    public float carbonValue; //no default
    public float nitrogenValue; //no default
    public float phosphateValue; //no default
    public float sulfurValue; //no default
    public float oxygenValue; //no default
    public float expressionThresholdValue = (float) 0.5;
    public float expressionUncertaintyValue = (float) 0.1;
    public int numberJobsValue = 1; //Default is one.
    public boolean randomValue = false;
    public boolean sampleValue = false;
    public boolean deleteResultsCard = false;
    public LinkedList<String> reactionKnockOutList;
    public LinkedList<String> geneKnockoutsList;
    public LinkedList<String> customFluxBoundsList;
    public LinkedList<String> mediaSupplementList;
    //GUI Components
    private JButton runDefaultSettingsButton;
    private JCheckBox fluxVariabilityAnalysis; //boolean, check is 1 unchecked is 0
    private JCheckBox simulateAllSingleKos; //boolean, check is 1 unchecked is 0
    private JCheckBox minimizeFlux; //boolean, check is 1 unchecked is 0
    private JTextField errorTextField; //Staying as a JTextfield
    //Float values GUI fields
    private JPlaceholderTextField activationCoefficientText;
    private JPlaceholderTextField carbonUptake;
    private JPlaceholderTextField nitrogenUptake;
    private JPlaceholderTextField phosphateUptake;
    private JPlaceholderTextField sulfurUptake;
    private JPlaceholderTextField oxygenUptake;
    private JPlaceholderTextField reactionToMaximize;
    private JPlaceholderTextField expressionThreshold;
    private JPlaceholderTextField expressionUncertainty;
    private JCheckBox randomCheckBox;
    private JCheckBox samplingCheckBox;
    private JPlaceholderTextField numberJobs;
    private JPlaceholderTextArea geneKnockouts;
    private JPlaceholderTextArea reactionKnockouts;
    private JCheckBox readFromFileCheckBox;
    public File userFile;
    private JPlaceholderTextArea mediaSupplement;
    private JPlaceholderTextArea customFluxBounds;
    private JPlaceholderTextField expressionCondition;
    private JCheckBox deleteResultsCheckBox;

    private boolean formError = false; //try catches will signal this.

    /*
    Main GUI Controller Function
     */
    public GUIForm(JobManager jobManager) {
        this.jobManager = jobManager;
        guiInitialization();
        mouseClickActivators();


        runDefaultSettingsButton.addActionListener(new ActionListener() {
            /*
            When the user presses the "run" button, We are going to save all the variables
            that they have input.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //randomValue will be 1 if the random box is selected, This means
                //We will bypass all other GUI checks.
                FBAParameters params;
                boolean readFromFile = false;
                randomValue = randomCheckBox.isSelected();
                sampleValue = samplingCheckBox.isSelected();
                if (readFromFileCheckBox.isSelected()) {
                    JFileChooser chooser = new JFileChooser();
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        userFile = chooser.getSelectedFile();
                        String absoluteFilePath = userFile.getAbsolutePath();
                        FileInputReader<FBAParameters> fbaFileInputReader = new JSONFileInputReader();
                        try {
                            Collection<FBAParameters> jobParams = fbaFileInputReader.parseFromFile(absoluteFilePath);
                            readFromFile = true;

                            for (FBAParameters jobParam : jobParams) {
                                jobManager.scheduleJob(new Job(jobParam));
                            }
                        } catch (FileNotFoundException | JobManagerStoppedException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                        //parseFromFile Json File input reader
                        //Example in app.java Line 100
                    }

                }
                if (!randomValue) {
                    guiValidator();
                    //Viewing the checklists of the 3 booleans and setting the values appropriately.
                    fluxVariabilityAnalysisValue = fluxVariabilityAnalysis.isSelected();
                    simulateAllSingleKosValue = simulateAllSingleKos.isSelected();
                    minimizeFluxValue = minimizeFlux.isSelected();
                    deleteResultsCard = deleteResultsCheckBox.isSelected();
                    //Close the Jpanel and free the resources it used.
                }
                if (!formError) {
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    // Create and queue a job from the user's inputs
                    try {
                        // Setup the parameters
                        if (!readFromFile) {
                            params = activateForm();
                            jobManager.scheduleJob(new Job(params));
                        }
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
    private void validationRange(double min, double max, Float userValue, String valueField) {
        if (!(userValue >= min && userValue <= max)) {
            formError = true;
            floatException(valueField, min, max);
        }
    }

    /*
    this code runs to validate if the float value is a integer value.
    if it is not an integer value, then the error message pops up and we stop the runner from executing
     until we have good values
     */
    private void floatException(String valueField, double min, double max) {
        errorTextField.setVisible(true);
        errorTextField.setForeground(Color.red);
        mainPanel.revalidate();
        mainPanel.repaint();
        if (min == 0 && max == 0) {
            errorTextField.setText(valueField + " must be an integer");
        } else {
            errorTextField.setText(valueField + " must be an integer between " + min + " - " + max + " inclusive");
        }
        formError = true;
    }

    /*
    method to group similar code for the on touch fields in the GUI.
    All the fields without a default can be done here.
     */
    private void onTouchListenerNoDefault(JTextField uptake, String uptakeName) {
        uptake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (uptake.getText().contains("*") || (uptake.getText().contains(uptakeName))) {
                    uptake.setText("");
                    uptake.setForeground(Color.BLACK);
                }
                // UptakeUpdateString = uptake.getText();
            }
        });
    }


    /*
    Iniitilize GUI Variables and Values
     */
    private void guiInitialization() {
        //Set checkboxes to true as this is the default
        fluxVariabilityAnalysis.setSelected(true);
        simulateAllSingleKos.setSelected(true);
        minimizeFlux.setSelected(true);
        add(mainPanel); //Display Panel
        setSize(500, 500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        activationCoefficientText.setPlaceHolderTextField("Activation Coefficient [0,1]");

        //Give all the text that "hint" look in the GUI
        activationCoefficientText.setForeground(Color.gray);
        carbonUptake.setPlaceHolderTextField("*Carbon Uptake [0,100]");
        nitrogenUptake.setPlaceHolderTextField("*Nitrogen Uptake [0,100]");
        phosphateUptake.setPlaceHolderTextField("*Phosphate Uptake [0,100]");
        sulfurUptake.setPlaceHolderTextField("*Sulfur Uptake [0,100]");
        oxygenUptake.setPlaceHolderTextField("*Oxygen Uptake [0,100]");
        reactionToMaximize.setPlaceHolderTextField("Reaction to Maximize");
        geneKnockouts.setPlaceHolderTextField("Gene Knockouts");
        expressionThreshold.setPlaceHolderTextField("Expression Threshold [0,1]");
        expressionUncertainty.setPlaceHolderTextField("Expression Uncertainty [0,?]");
        numberJobs.setPlaceHolderTextField("Number of Jobs");
        customFluxBounds.setPlaceHolderTextField("Custom Flux Bounds");
        reactionKnockouts.setPlaceHolderTextField("ReactionKnockouts");
        mediaSupplement.setPlaceHolderTextField("Media Supplement");
        expressionCondition.setPlaceHolderTextField("Expression Condition");


    }

    /*
    Method used for checking that values exist where required and makes sure they are within
    the specified range.
    Used in guiValidator
     */
    public float guiValidationCheck(String element, String elementDefault, String errorMessage,
                                    float elementValue, int min, int max, String defaultString) {
        try {
            //Check if the user left the value as a default value
            if (element.equals(elementDefault) || element.equals("")) {
                //this value can't be left blank, no specified default
                if (sampleValue) {
                    elementValue = getRandInRange(min, max);
                } else {
                    formError = true;
                    errorTextField.setText(errorMessage);
                }
            } else {
                elementValue = Float.parseFloat(element); //if not default set as user value
            }

        } catch (NumberFormatException k) {
            floatException(defaultString, min, max);
        }
        validationRange(min, max, elementValue, defaultString);
        return elementValue;
    }

    private void guiValidator() {
        formError = false; //update if any variables fail the conditional checks

        //Set fields at time of button pressed
        activationCoefficientString = activationCoefficientText.getText();
        numberJobsString = numberJobs.getText();
        carbonString = carbonUptake.getText();
        phosphateString = phosphateUptake.getText();
        nitrogenString = nitrogenUptake.getText();
        sulfurString = sulfurUptake.getText();
        oxygenString = oxygenUptake.getText();
        expressionthresholdString = expressionThreshold.getText();
        expressionUncertaintyString = expressionUncertainty.getText();
        reactionToMaximizeString = reactionToMaximize.getText();
        geneKnockoutsString = geneKnockouts.getText();
        mediaSupplementString = mediaSupplement.getText();
        expressionConditionString = expressionCondition.getText();


        try {
            //Check if the user left the value as a default value
            if (activationCoefficientString.equals("")
                    || activationCoefficientString.equals("Activation Coefficient [0,1]")) {
                if (sampleValue) {
                    activationCoefficient = getRandInRange(0, 1);
                } else {
                    activationCoefficient = (float) 0.5;
                }
            } else if (!activationCoefficientString.equals("Activation Coefficient [0,1]")) {
                activationCoefficient = Float.parseFloat(activationCoefficientString);
                //if not default set as user value
            }

        } catch (NumberFormatException k) {
            floatException("Activation Coefficient", 0, 1);
        }
        validationRange(0.0, 1.0, activationCoefficient, "Activation Coefficient");

        try {
            if (numberJobsString.equals("") || numberJobsString.equals("Number of Jobs")) {
                numberJobsValue = 1; //Default value
            } else if (!numberJobsString.equals("Number of Jobs")) {
                numberJobsValue = Integer.parseInt(numberJobsString);
            }
        } catch (NumberFormatException k) {
            floatException("Number of Jobs", 0, 0);
        }


        //Setting the Carbon string from the GUI for the web driver
        carbonValue = guiValidationCheck(carbonString, "*Carbon Uptake [0,100]",
                "Carbon Uptake Field is required. Range: 0-100", carbonValue, 0, 100,
                "Carbon Uptake");

        //Phosphate code base
        phosphateValue = guiValidationCheck(phosphateString, "*Phosphate Uptake [0,100]",
                "Phosphate Uptake Field is required. Range: 0-100", phosphateValue, 0,
                100, "Phosphate Uptake");

        //Nitrogen Code base
        nitrogenValue = guiValidationCheck(nitrogenString, "*Nitrogen Uptake [0,100]",
                "Nitrogen Uptake Field is required. Range: 0-100", nitrogenValue, 0, 100,
                "Nitrogen Uptake");

        //Sulfur Code base
        sulfurValue = guiValidationCheck(sulfurString, "*Sulfur Uptake [0,100]",
                "Sulfur Uptake Field is required. Range: 0-100", sulfurValue, 0, 100,
                "Sulfur Uptake");

        oxygenValue = guiValidationCheck(oxygenString, "*Oxygen Uptake [0,100]",
                "Oxygen Uptake Field is required. Range: 0-100", oxygenValue, 0, 100,
                "Oxygen Uptake");

        try {
            //Check if the user left the value as a default value
            if (expressionthresholdString.equals("")
                    || expressionthresholdString.equals("Expression Threshold [0,1]")) {
                if (sampleValue && (expressionthresholdString.equals("")
                        || expressionthresholdString.equals("Expression Threshold [0,1]"))) {
                    expressionThresholdValue = getRandInRange(0, 1);
                } else {
                    expressionThresholdValue = (float) 0.5;
                }
            } else if (!expressionthresholdString.equals("Expression Threshold [0,1]")) {
                expressionThresholdValue = Float.parseFloat(expressionthresholdString);
                //if not default set as user value
            }

        } catch (NumberFormatException k) {
            floatException("Expression Threshold", 0, 1);
        }
        validationRange(0.0, 1.0, expressionThresholdValue, "Expression Threshold");

        try {
            //Check if the user left the value as a default value
            if (expressionUncertaintyString.equals("")
                    || expressionUncertaintyString.equals("Expression Uncertainty [0,?]")) {
                if (sampleValue && (expressionUncertaintyString.equals("")
                        || expressionUncertaintyString.equals("Expression Uncertainty [0,?]"))) {
                    expressionUncertaintyValue = getRandInRange(0, 1);
                } else {
                    expressionUncertaintyValue = (float) 0.5;
                }
            } else if (!expressionUncertaintyString.equals("Expression Uncertainty [0,?]")) {
                expressionUncertaintyValue = Float.parseFloat(expressionUncertaintyString);
                //if not default set as user value
            }

        } catch (NumberFormatException k) {
            floatException("Expression Uncertainty", 0, Integer.MAX_VALUE);
        }
        validationRange(0.0, Integer.MAX_VALUE, expressionUncertaintyValue,
                "Expression Uncertainty");

        //reaction Knockouts
        //intilize linkedlists
        reactionKnockOutList = new LinkedList<String>();
        String reactionKnockOutString = reactionKnockouts.getText();
        Scanner reactionKnockOutScanner = new Scanner(reactionKnockOutString);
        reactionKnockOutScanner.useDelimiter("\n");
        while (reactionKnockOutScanner.hasNext()) {
            reactionKnockOutList.add(reactionKnockOutScanner.next());
        }
        reactionKnockOutScanner.close();

        geneKnockoutsList = new LinkedList<String>();
        Scanner geneKnockOutScanner = new Scanner(geneKnockoutsString);
        geneKnockOutScanner.useDelimiter("\n");
        while (geneKnockOutScanner.hasNext()) {
            geneKnockoutsList.add(geneKnockOutScanner.next());
        }
        geneKnockOutScanner.close();

        customFluxBoundsList = new LinkedList<String>();
        String customFluxBoundsString = customFluxBounds.getText();
        Scanner customFluxBoundsScanner = new Scanner(customFluxBoundsString);
        customFluxBoundsScanner.useDelimiter("\n");
        while (customFluxBoundsScanner.hasNext()) {
            customFluxBoundsList.add(customFluxBoundsScanner.next());
        }
        customFluxBoundsScanner.close();

        mediaSupplementList = new LinkedList<String>();
        String mediaSupplementString = mediaSupplement.getText();
        Scanner mediaSupplementScanner = new Scanner(mediaSupplementString);
        mediaSupplementScanner.useDelimiter("\n");
        while (mediaSupplementScanner.hasNext()) {
            mediaSupplementList.add(mediaSupplementScanner.next());
        }
        mediaSupplementScanner.close();
    }

    /*
    Set Runner values after extracting values from the GUI
     */
    private void setRunnerParameters(FBAParameters params) {
        params.setActivationCoefficient(activationCoefficient);
        params.setReactionToMaximize(reactionToMaximizeString);
        params.setMinimizeFlux(minimizeFluxValue);
        params.setFluxVariabilityAnalysis(fluxVariabilityAnalysisValue);
        params.setSimulateAllSingleKos(simulateAllSingleKosValue);
        params.setActivationCoefficient(activationCoefficient);
        params.setMaxCarbonUptake(carbonValue);
        params.setMaxNitrogenUptake(nitrogenValue);
        params.setMaxPhosphateUptake(phosphateValue);
        params.setMaxSulfurUptake(sulfurValue);
        params.setMaxOxygenUptake(oxygenValue);
        params.setExpressionThreshold(expressionThresholdValue);
        params.setExpressionUncertainty(expressionUncertaintyValue);
        params.setGeneKnockouts(geneKnockoutsList);
        params.setCustomFluxBounds(customFluxBoundsList);
        params.setReactionKnockouts(reactionKnockOutList);
        params.setMediaSupplements(mediaSupplementList);
        params.setExpressionCondition(expressionConditionString);
        params.setDeleteCard(deleteResultsCard);
    }

    private void randomChecked(FBAParameters params) {
        params.setActivationCoefficient(getRandInRange(0, 1));
        params.setReactionToMaximize(reactionToMaximizeString); //Not a randomizable variable
        params.setMinimizeFlux(getRandBoolean(0, 1));
        params.setSimulateAllSingleKos(getRandBoolean(0, 1));
        params.setFluxVariabilityAnalysis(getRandBoolean(0, 1));
        params.setActivationCoefficient(getRandInRange(0, 1));
        params.setMaxCarbonUptake(getRandInRange(0, 100));
        params.setMaxNitrogenUptake(getRandInRange(0, 100));
        params.setMaxPhosphateUptake(getRandInRange(0, 100));
        params.setMaxSulfurUptake(getRandInRange(0, 100));
        params.setMaxOxygenUptake(getRandInRange(0, 100));
        params.setExpressionThreshold(getRandInRange(0, 1));
        params.setExpressionUncertainty(getRandInRange(0, 100)); //MAY NEED TO CHANGE THE MAX
    }

    /*
    Method to simplify the main GUI method.
    Putting all the Mouse click setup in here to make the "Main"
    method simplified.
     */
    private void mouseClickActivators() {
        samplingCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                sampleValue = samplingCheckBox.isSelected();
                if (sampleValue) {
                    errorTextField.setText("Fill in the fields to test the rest will be randomized.");
                }
            }
        });

    }

    public FBAParameters activateForm() {
        FBAParameters params;
        if (samplingCheckBox.isSelected()) {
            params = new FBAParameters(fluxVariabilityAnalysisValue,
                    minimizeFluxValue, simulateAllSingleKosValue, activationCoefficient,
                    carbonValue, nitrogenValue, phosphateValue, sulfurValue, oxygenValue,
                    reactionToMaximizeString, expressionThresholdValue, expressionUncertaintyValue,
                    geneKnockoutsList, mediaSupplementList, customFluxBoundsList, new LinkedList<>(), true, "");
        } else if (randomValue) {
            params = new FBAParameters(randomValue);
            randomChecked(params);
        } else {
            params = new FBAParameters(fluxVariabilityAnalysisValue,
                    minimizeFluxValue, simulateAllSingleKosValue, activationCoefficient,
                    carbonValue, nitrogenValue, phosphateValue, sulfurValue, oxygenValue,
                    reactionToMaximizeString, expressionThresholdValue, expressionUncertaintyValue,
                    geneKnockoutsList, mediaSupplementList, customFluxBoundsList, new LinkedList<>(), true, "");
            //duplicate code?
            setRunnerParameters(params);
            // Queue the job
        }
        return params;
    }

    public int getNumberJobs() {
        return numberJobsValue;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
