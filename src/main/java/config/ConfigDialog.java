package config;

import util.AbstractDialog;

import javax.swing.*;

public class ConfigDialog extends AbstractDialog {
    public ConfigDialog() {
        super();
        this.setSize(640, 480);
        this.setTitle("Konfiguracja aplikacji");
        this.setLocationRelativeTo(null); // center the dialog.

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        this.setVisible(true);
    }
}
