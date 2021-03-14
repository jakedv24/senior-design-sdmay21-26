package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Automation routines for KBase which are not application-specific.
 *
 * For the FBA application, see the parameter specification here:
 * https://github.com/cshenry/fba_tools/blob/master/ui/narrative/methods/run_flux_balance_analysis/spec.json
 *
 * Note the distinction between "text" and "option" below:
 *   Text: free-form input
 *   Option: selection from pre-defined options
 */
public class KBaseHelper {
    /**
     * Sets the value of a checkbox input.
     *
     * @param codeCell The KBase code cell encompassing the checkbox.
     * @param parameterName The value of "data-parameter" in the surrounding div.
     * @param value The new checkbox value (true checked, false unchecked).
     */
    public static void setCheckBox(WebElement codeCell, String parameterName, boolean value) {
        // Checkbox inputs are wrapped by a div which includes an identifier for the particular parameter
        WebElement checkbox = codeCell
                .findElement(By.cssSelector("div[data-parameter='" + parameterName + "']"))
                .findElement(By.cssSelector("input[type='checkbox']"));

        // If the checkbox doesn't match the set value, toggle it
        if (checkbox.isSelected() != value) {
            checkbox.click();
        }
    }

    /**
     * Sets the value of a single-value text input.
     */
    public static void setTextBox() {}

    /**
     * Sets the value(s) of a multi-value text input.
     */
    public static void setTextList() {}

    /**
     * Sets the value(s) of a searchable multi-value option input.
     */
    public static void setSearchableTextList() {}
}
