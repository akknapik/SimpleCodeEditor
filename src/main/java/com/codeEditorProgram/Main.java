package com.codeEditorProgram;

import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkFlatIJTheme.setup();

        try {
            UIManager.setLookAndFeel( new FlatDarkFlatIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize UIManager" );
        }

        MainWindow codeEditor = new MainWindow();
    }
}
