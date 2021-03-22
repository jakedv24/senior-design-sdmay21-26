package edu.iastate.ece.sd.sdmay2126.runner.gui.application.fluxbalanceanalysis;

import edu.iastate.ece.sd.sdmay2126.orchestration.Job;
import edu.iastate.ece.sd.sdmay2126.runner.gui.selenium.SeleniumResultsCardDeleter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FBAResultsCardDeleter implements SeleniumResultsCardDeleter {
    WebDriver driver;

    public FBAResultsCardDeleter(WebDriver driver) {
        this.driver = driver;
    }


    @Override
    public void DeleteResultsCard() {
        List<WebElement> cards = driver.findElements(By.className("code_cell"));
        for (WebElement card : cards) {
            WebElement title = card.findElement(By.className("title"));
            if (title.getText().contains("Output from Run Flux Balance Analysis")) {
                System.out.println("Found output cell");
            }
        }

    }
}

