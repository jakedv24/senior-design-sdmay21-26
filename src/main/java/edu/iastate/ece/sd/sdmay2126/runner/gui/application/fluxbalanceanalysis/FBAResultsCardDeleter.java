package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;
import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumIdentificationException;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumKBaseHelper;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumResultsCardDeleter;
import org.openqa.selenium.WebDriver;





public class FBAResultsCardDeleter implements SeleniumResultsCardDeleter {
    WebDriver driver;

    public FBAResultsCardDeleter(WebDriver driver) {
        this.driver = driver;
    }


    @Override
    public void deleteResultsCard(Job job) throws InterruptedException, SeleniumIdentificationException {
        FBAParameters params = (FBAParameters) job.getParameters();
        if (params.getDeleteCard()) {
            SeleniumKBaseHelper.deleteResultCard("Output from Run Flux Balance Analysis", driver);

        }

    }


}

