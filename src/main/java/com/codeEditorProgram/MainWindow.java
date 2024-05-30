package com.codeEditorProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainWindow extends JFrame {

    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;

    MainWindow() {
        frame = new JFrame("Code Editor - Nowy plik");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
        frame.add(scrollPane);
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        createMenu(frame);
        frame.setVisible(true);
    }

    private void createMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Plik");

        JMenuItem createNewItem = new JMenuItem("Nowy");
        createNewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFile();
            }
        });

        JMenuItem openItem = new JMenuItem("Otwórz");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        JMenuItem saveItem = new JMenuItem("Zapisz");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        JMenuItem saveItemAs = new JMenuItem("Zapisz jako");
        saveItemAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        });

        fileMenu.add(createNewItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveItemAs);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
    }

    private void createNewFile() {
        int option = JOptionPane.showConfirmDialog(this, "Czy chcesz zapisać zmiany?");
        if(option == JOptionPane.YES_OPTION) {
            saveFile();
        }
        textArea.setText("");
        currentFile = null;
        setWindowTitle("Nowy plik");
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            String fileName = currentFile.getName();

            if(fileName.endsWith(".java") | fileName.endsWith(".txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                    textArea.read(reader, null);
                    setWindowTitle(currentFile.getName());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Nieobsługiwany format pliku!");
            }
        }
    }

    private void saveFile() {
        if(currentFile != null) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                setWindowTitle(currentFile.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        int returnValue = fileChooser.showOpenDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            String fileName = currentFile.getName();
            if (!fileName.toLowerCase().endsWith(".java")) {
                fileName += ".java";
                currentFile = new File(currentFile.getParent(), fileName);
            }
            saveFile();
            setWindowTitle(currentFile.getName());
        }
    }

    private void setWindowTitle(String fileName) {
        frame.setTitle("Code Editor - " + fileName);
    }

    private void onClose() {
        int option = JOptionPane.showConfirmDialog(this, "Czy chcesz zapisać zmiany przed zamknięciem?");
        if(option == JOptionPane.YES_OPTION) {
            saveFile();
            dispose();
        } else if (option == JOptionPane.NO_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        MainWindow codeEditor = new MainWindow();
    }
}
