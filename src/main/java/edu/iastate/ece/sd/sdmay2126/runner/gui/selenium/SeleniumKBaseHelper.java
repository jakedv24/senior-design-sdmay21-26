package edu.iastate.ece.sd.sdmay2126.runner.gui.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Automation routines for KBase which are not application-specific.
 * <p>
 * For the FBA application, see the parameter specification here:
 * https://github.com/cshenry/fba_tools/blob/master/ui/narrative/methods/run_flux_balance_analysis/spec.json
 * <p>
 * Note the distinction between "text" and "option" below:
 * Text: free-form input
 * Option: selection from pre-defined options
 */
public class SeleniumKBaseHelper {
    /**
     * Used to identify a particular parameter from the code cell.
     */
    private static final String PARAM_SELECTOR = "div[data-parameter='%s']";

    /**
     * Sets the value of a checkbox input.
     *
     * @param codeCell      The KBase code cell encompassing the checkbox.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param value         The new checkbox value (true checked, false unchecked).
     */
    public static void setCheckBox(WebElement codeCell, String parameterName, boolean value) {
        System.out.printf("Setting checkbox \"%s\" to %s...%n", parameterName, value);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAM_SELECTOR, parameterName)));
        WebElement checkboxInput = parameterRow.findElement(By.cssSelector("input[type='checkbox']"));

        // If the checkbox doesn't match the set value, toggle it
        if (checkboxInput.isSelected() != value) {
            checkboxInput.click();
        }
    }

    /**
     * Sets the value of a single-value text input.
     *
     * @param codeCell      The KBase code cell encompassing the textbox.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param value         The new textbox value.
     */
    public static void setTextBox(WebElement codeCell, String parameterName, String value) {
        System.out.printf("Setting textbox \"%s\" to value \"%s\"...%n", parameterName, value);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAM_SELECTOR, parameterName)));
        WebElement textInput = parameterRow.findElement(By.cssSelector("input[class='form-control']"));

        // Clear and re-write text
        textInput.clear();
        textInput.sendKeys(value);
    }

    /**
     * Sets the value(s) of a multi-value text input.
     *
     * @param driver        The driver for waiting on changes.
     * @param codeCell      The KBase code cell encompassing the list input.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param values        The list item values.
     * @throws SeleniumIdentificationException If unable to find added list item.
     */
    public static void setTextList(WebDriver driver, WebElement codeCell, String parameterName, List<String> values)
            throws SeleniumIdentificationException, InterruptedException {
        System.out.printf("Setting text list \"%s\"...%n", parameterName);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAM_SELECTOR, parameterName)));
        List<WebElement> listItems;

        // Remove previous items from list
        listItems = parameterRow.findElements(By.cssSelector("div[data-element='input-row']"));
        System.out.printf("  Removing %d items...%n", listItems.size());
        for (WebElement previousRow : listItems) {
            previousRow.findElement(By.cssSelector("span[class='fa fa-close']")).click();
        }

        // Add new items to list
        System.out.printf("  Adding %d items...%n", values.size());
        int itemsAdded = 0;
        for (String value : values) {
            // Click the "plus" to add new item
            parameterRow.findElement(By.cssSelector("button[class='btn btn-default']")).click();

            // Grab items and identify the last (just-added) item (wait for JS from previous click to complete)
            listItems = SeleniumUtilities.waitForNMatches(driver, By.cssSelector("div[data-element='input-row']"),
                    ++itemsAdded, Duration.ofSeconds(10), parameterRow);
            WebElement lastItem = listItems.get(listItems.size() - 1);

            // Find the item's text field and enter our value
            WebElement itemInput = lastItem.findElement(By.cssSelector("input[class='form-control']"));
            itemInput.clear();
            itemInput.sendKeys(value);
        }
    }

    /**
     * Sets the value(s) of a searchable multi-value option input.
     *
     * @param codeCell      The KBase code cell encompassing the options input.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param values        The list item values.
     */
    public static void setSearchableOptionList(WebElement codeCell, String parameterName, List<String> values) {
        if (values == null) {
            System.out.println(parameterName + " is null, skipping...");
            return;
        }

        System.out.printf("Setting option list \"%s\"...%n", parameterName);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAM_SELECTOR, parameterName)));

        WebElement selectedItemsPane = parameterRow
                .findElement(By.cssSelector("div[data-element='selected-items']"));
        WebElement availableItemsPane = parameterRow
                .findElement(By.cssSelector("div[data-element='available-items-area']"));

        // Clear previous selections
        List<WebElement> selectedItems = selectedItemsPane.findElements(By.cssSelector(".row"));
        System.out.printf("  Removing %d items...%n", selectedItems.size());
        while (!selectedItems.isEmpty()) {
            selectedItems.get(0).findElement(By.cssSelector("span[class='fa fa-minus-circle']")).click();

            // Reload the remaining items so our references don't become stale
            selectedItems = selectedItemsPane.findElements(By.cssSelector(".row"));
        }

        // Make new selections
        WebElement searchField = availableItemsPane.findElement(By.cssSelector("input[class='form-contol']"));
        System.out.printf("  Adding %d items...%n", values.size());
        for (String value : values) {
            if (value == null) {
                System.out.println("a value for " + parameterName + " is null, skipping value...");
                break;
            }
            // Search for option
            searchField.clear();
            searchField.sendKeys(value);

            // Expect and click a single result
            List<WebElement> searchResults = availableItemsPane
                    .findElements(By.cssSelector("span[class='kb-btn-icon']"));
            if (searchResults.size() == 1) {
                searchResults.get(0).click();
            } else if (searchResults.isEmpty()) {
                // TODO: Should this be an exception? Currently, there's no way to detect/handle it from a higher level
                System.err.printf("  Item could not be found: %s%n", value);
            } else {
                // TODO: Should this be an exception? Currently, there's no way to detect/handle it from a higher level
                System.err.printf("  Item has multiple matches: %s%n", value);
            }
        }
    }

    public static void deleteCardByName(String cardName, WebDriver driver)
            throws InterruptedException, SeleniumIdentificationException {
        List<WebElement> cards = driver.findElements(By.className("code_cell"));
        for (WebElement card : cards) {
            WebElement title = card.findElement(By.className("title"));
            if (title.getText().contains(cardName)) {
                deleteCardByElement(card, driver);
                break;
            }
        }
    }

    public static void deleteCardByElement(WebElement codeCell, WebDriver driver)
            throws SeleniumIdentificationException, InterruptedException {
        WebElement optionBox = codeCell
                .findElement(By.cssSelector("button[class='btn btn-xs btn-default dropdown-toggle']"));
        optionBox.click();
        List<WebElement> buttons = codeCell.findElements(By.cssSelector("button[class='btn btn-default']"));
        for (WebElement button : buttons) {
            List<WebElement> spanners = button.findElements(By.tagName("span"));
            for (WebElement span : spanners) {
                if (span.getText().contains("Delete cell")) {
                    button.click();
                }
            }
        }

        acceptKbasePopUp(driver);
        SeleniumUtilities.clickUntilSuccessful(driver
                .findElement(By.cssSelector("button[id='kb-save-btn']")), Duration.ofSeconds(20));

    }

    private static void acceptKbasePopUp(WebDriver driver)
            throws SeleniumIdentificationException, InterruptedException {
        WebElement popUp = driver.findElement(By.cssSelector("div[class='modal-footer']"));
        WebElement yesButton = popUp.findElement(By.cssSelector("button[class='btn btn-primary']"));
        SeleniumUtilities.clickUntilSuccessful(yesButton, Duration.ofSeconds(20));

    }

    public static void resetCardIfNeeded(WebElement codeCell, WebDriver driver)
            throws InterruptedException, SeleniumIdentificationException {
        System.out.println("Checking if card needs to be reset...");
        List<WebElement>  resetButton = codeCell.findElements(By.cssSelector("button[class='btn btn-default -rerun"));
        if (resetButton.isEmpty()) {
            System.out.println("No reset required...");
            return;
        }
        resetButton.get(0).click();
        acceptKbasePopUp(driver);
        SeleniumUtilities.clickUntilSuccessful(codeCell, Duration.ofSeconds(10));
        System.out.println("Card has been reset...");

    }

}
