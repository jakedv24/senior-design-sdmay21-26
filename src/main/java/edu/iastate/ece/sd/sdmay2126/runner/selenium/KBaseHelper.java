package edu.iastate.ece.sd.sdmay2126.runner.selenium;

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
     */
    public static void setCheckBox() {}

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
