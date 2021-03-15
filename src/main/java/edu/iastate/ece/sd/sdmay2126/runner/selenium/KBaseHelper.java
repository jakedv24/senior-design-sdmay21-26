package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Automation routines for KBase which are not application-specific.
 *
 * For the FBA application, see the parameter specification here:
 * https://github.com/cshenry/fba_tools/blob/master/ui/narrative/methods/run_flux_balance_analysis/spec.json
 *
 * Note the distinction between "text" and "option" below:
 * Text: free-form input
 * Option: selection from pre-defined options
 */
public class KBaseHelper {
    /**
     * Used to identify a particular parameter from the code cell.
     */
    private static final String PARAMETER_SELECTOR = "div[data-parameter='%s']";

    /**
     * Sets the value of a checkbox input.
     *
     * @param codeCell      The KBase code cell encompassing the checkbox.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param value         The new checkbox value (true checked, false unchecked).
     */
    public static void setCheckBox(WebElement codeCell, String parameterName, boolean value) {
        System.out.printf("Setting checkbox \"%s\" to %s...%n", parameterName, value);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAMETER_SELECTOR, parameterName)));
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

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAMETER_SELECTOR, parameterName)));
        WebElement textInput = parameterRow.findElement(By.cssSelector("input[class='form-control']"));

        // Clear and re-write text
        textInput.clear();
        textInput.sendKeys(value);
    }

    /**
     * Sets the value(s) of a multi-value text input.
     */
    public static void setTextList(WebElement codeCell, String parameterName, List<String> values) {
        System.out.printf("Setting text list \"%s\"...%n", parameterName);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAMETER_SELECTOR, parameterName)));
        List<WebElement> listItems;

        // Remove previous items from list
        listItems = parameterRow.findElements(By.cssSelector("div[data-element='input-row']"));
        System.out.printf("  Removing %d items...%n", listItems.size());
        for (WebElement previousRow : listItems) {
            previousRow.findElement(By.cssSelector("span[class='fa fa-close']")).click();
        }

        // Add new items to list
        System.out.printf("  Adding %d items...%n", values.size());
        for (String value : values) {
            // Click the "plus" to add new item
            parameterRow.findElement(By.cssSelector("button[class='btn btn-default']")).click();

            // Grab items and identify the last (just-added) item
            listItems = parameterRow.findElements(By.cssSelector("div[data-element='input-row']"));
            WebElement lastItem = listItems.get(listItems.size() - 1);

            // Find the item's text field and enter our value
            WebElement itemInput = lastItem.findElement(By.cssSelector("input[class='form-control']"));
            itemInput.clear();
            itemInput.sendKeys(value);
        }
    }

    /**
     * Sets the value(s) of a searchable multi-value option input.
     */
    public static void setSearchableOptionList(WebElement codeCell, String parameterName, List<String> values) {
        System.out.printf("Setting option list \"%s\"...%n", parameterName);

        WebElement parameterRow = codeCell.findElement(By.cssSelector(String.format(PARAMETER_SELECTOR, parameterName)));

        // Clear previous selections
        WebElement selectedItemsPane = parameterRow.findElement(By.cssSelector("div[data-element='selected-items']"));
        List<WebElement> selectedItems = selectedItemsPane.findElements(By.cssSelector(".row"));
        System.out.printf("  Removing %d items...%n", selectedItems.size());
        for (WebElement previousOption : selectedItems) {
            previousOption.findElement(By.cssSelector("span[class='fa fa-minus-circle']")).click();
        }

        // Make new selections
        WebElement availableItemsPane = parameterRow.findElement(By.cssSelector("div[data-element='available-items']"));
        WebElement searchField = availableItemsPane.findElement(By.cssSelector("input[class='form-contol']"));
        System.out.printf("  Adding %d items...%n", values.size());
        for (String value : values) {
            // Search for option
            searchField.clear();
            searchField.sendKeys(value);

            // Expect and click a single result
            List<WebElement> searchResults = availableItemsPane.findElements(By.cssSelector("span[class='kb-btn-icon']"));
            if (searchResults.size() == 1) {
                searchResults.get(0).click();
            } else if (searchResults.isEmpty()) {
                // TODO: Should this be an exception? Currently, there's no way to detect/handle it from a higher level
                System.err.printf("  Item could not be found: %s%n" + value);
            } else {
                // TODO: Should this be an exception? Currently, there's no way to detect/handle it from a higher level
                System.err.printf("  Item has multiple matches: %s%n" + value);
            }
        }
    }
}
