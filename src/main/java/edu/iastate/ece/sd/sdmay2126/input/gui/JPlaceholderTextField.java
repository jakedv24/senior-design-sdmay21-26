package edu.iastate.ece.sd.sdmay2126.input.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A JTextArea with placeholder text.
 */
public class JPlaceholderTextField extends JTextField {
    private final String placeholderText;

    /**
     * @param placeholderText The placeholder text for this field.
     */
    public JPlaceholderTextField(String placeholderText) {
        this.placeholderText = placeholderText;

        setText(placeholderText);
        setForeground(Color.GRAY);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholderText)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholderText);
                    setForeground(Color.GRAY);
                }
            }
        });
    }
}