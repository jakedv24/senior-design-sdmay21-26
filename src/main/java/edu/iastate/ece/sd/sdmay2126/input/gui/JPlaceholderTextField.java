package edu.iastate.ece.sd.sdmay2126.input.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A JTextArea with placeholder text.
 */
public class JPlaceholderTextField extends JTextField {
    private String placeholderText;

    /**
     *
     */
    public JPlaceholderTextField() {
        this.placeholderText = "";


        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(JPlaceholderTextField.this.placeholderText)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(JPlaceholderTextField.this.placeholderText);
                    setForeground(Color.GRAY);
                }
            }
        });
    }

    public void setPlaceHolderTextField(String text) {
        this.placeholderText = text;
        setText(placeholderText);
        setForeground(Color.GRAY);
    }
}
