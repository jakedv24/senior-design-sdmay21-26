package edu.iastate.ece.sd.sdmay2126;

import edu.iastate.ece.sd.sdmay2126.orchestration.JobManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    public JPanel MainPanel;
    private JTextField ErrorTextField;
    public boolean fluxVariabilityAnalysisValue = false; //Value read from the checkbox.
    public boolean simulateAllSingleKosValue = false; //Value read from the checkbox.
    public boolean minimizeFluxValue = false; //Value read from the checkbox.
    private boolean formError = false; //try catches will signal this.

    //TODO (DL): Set error message to red, set error message if number is outside of the range, set booleans to values, handoff to driver.
    //TODO (DL) Cont, comment new additions to code.
    public GUIForm(JobManager jobManager) {
        this.jobManager = jobManager;

        add(MainPanel); //Display Panel
        setSize(500, 500); //Set a arbitrary size for the GUI
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Adds the X button to close
        activationCoefficientText.setText("Activation Coefficient [0,1]");
        activationCoefficientText.setForeground(Color.gray);
        ErrorTextField.setVisible(false);
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
                    activationCoefficient = Float.parseFloat(activationCoefficientString);
                } catch (NumberFormatException k) {
                    ErrorTextField.setVisible(true);
                    //TODO color change no working, Make error message more noticeable.
                    ErrorTextField.setSelectionColor(Color.red);
                    MainPanel.revalidate();
                    MainPanel.repaint();
                    ErrorTextField.setText("Activation Coefficient must be an integer between 0-1 inclusive");
                    formError = true;
                }
                //Viewing the checklists of the 3 booleans and setting the values appropriately.
                fluxVariabilityAnalysisValue = fluxVariabilityAnalysis.isSelected();
                simulateAllSingleKosValue = simulateAllSingleKos.isSelected();
                minimizeFluxValue = minimizeFlux.isSelected();
                //Close the Jpanel and free the resources it used.
                if (formError == false) {
                    JComponent comp = (JComponent) e.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WebDriver driver = App.getDriver();
                            try {
                                System.out.println("Loading KBase narrative...");
                                driver.get("https://narrative.kbase.us/narrative/72313");

                                // Let things load
                                Thread.sleep(3000);

                                // Login with Globus
                                App.performGlobusAuthFlow(driver);

                                // This load really takes a while
                                Thread.sleep(15000);

                                // TODO: Interact with the narrative (fill form, click buttons, read output)
                                //Identify the third textbox and clicks it note if the textboex are rearanged on kbase this will break
                                WebElement codeBox = new WebDriverWait(driver, 10).until(d -> d.findElements(By.cssSelector("div[class^='cell code_cell']"))).get(2);
                                codeBox.click();


                                //There are two buttons that show and hide adnaved options we want to click the second one
                                new WebDriverWait(driver, 10).until(d -> codeBox.findElements(By.cssSelector("button[title='show advanced']"))).get(1).click();


                                //WebElement fluxAnalysisBox = driver.findElements(By.cssSelector("div[class^='cell code_cell")).get(2);
                                //fluxAnalysisBox.click();

                                //the check boxes are children of divs
                                WebElement fvaParent = driver.findElement(By.cssSelector("div[data-parameter='fva']"));
                                WebElement fva = fvaParent.findElement(By.cssSelector("input[type='checkbox']"));
                                //if the check box doesn't match what is selected the it is clicked
                                if (fva.isSelected() != fluxVariabilityAnalysisValue) {

                                    fva.click();
                                }

                                WebElement minFluxParent = driver.findElement(By.cssSelector("div[data-parameter='minimize_flux']"));
                                WebElement minFlux = minFluxParent.findElement(By.cssSelector("input[type='checkbox']"));
                                if (minFlux.isSelected() != minimizeFluxValue) {
                                    System.out.println("clicking flux");

                                    minFlux.click();
                                }

                                WebElement simKoParent = driver.findElement(By.cssSelector("div[data-parameter='simulate_ko']"));
                                WebElement simKo = simKoParent.findElement(By.cssSelector("input[type='checkbox']"));
                                if (simKo.isSelected() != simulateAllSingleKosValue) {

                                    simKo.click();
                                }


                                //finds clear and sets the value of the activeCoefficient value
                                WebElement activCoeParent = driver.findElement(By.cssSelector("div[data-parameter='activation_coefficient']"));
                                WebElement activCoe = activCoeParent.findElement(By.cssSelector("input[class='form-control']"));
                                activCoe.clear();
                                activCoe.sendKeys(activationCoefficientString);


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
            }
        });

    }

}
