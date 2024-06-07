package com.codeEditorProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;


public class MainWindow extends JFrame {

    private JFrame frame;
    private JTextArea textArea;
    private JTextArea consoleArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private JButton runButton;
    private JButton stopButton;
    private Process currentProcess;
    private Thread runThread;

    MainWindow() {
        frame = new JFrame("Code Editor - Nowy plik");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.WHITE);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Twój kod"));

        consoleArea = new JTextArea();
        consoleArea.setBackground(Color.DARK_GRAY);
        consoleArea.setForeground(Color.WHITE);

        consoleArea.setEditable(false);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setPreferredSize(new Dimension(screenWidth, screenHeight / 4));
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Konsola"));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textScrollPane, consoleScrollPane);
        splitPane.setDividerLocation((int) (screenHeight * 0.75));
        splitPane.setResizeWeight(0.75);

        frame.add(splitPane);
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        createMenu(frame);
        var listener = new Terminator();
        frame.addWindowListener(listener);
        frame.setVisible(true);
    }

    private void createMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Plik");
        JMenu viewMenu = new JMenu("Widok");
        JMenu colorMenu = new JMenu("Kolor tła");
        runButton = new JButton("Uruchom");
        stopButton = new JButton("Zatrzymaj");

        JMenuItem createNewItem = new JMenuItem("Nowy");
        createNewItem.addActionListener(e -> createNewFile());

        JMenuItem openItem = new JMenuItem("Otwórz");
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Zapisz");
        saveItem.addActionListener(e -> saveFile());

        JMenuItem saveItemAs = new JMenuItem("Zapisz jako");
        saveItemAs.addActionListener(e -> saveFileAs());

        JMenuItem setBgColorDark = new JMenuItem("Ciemny motyw");
        setBgColorDark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.WHITE);
                consoleArea.setBackground(Color.DARK_GRAY);
                consoleArea.setForeground(Color.WHITE);
            }
        });

        JMenuItem setBgColorWhite = new JMenuItem("Jasny motyw");
        setBgColorWhite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(Color.WHITE);
                textArea.setForeground(Color.BLACK);
                consoleArea.setBackground(Color.WHITE);
                consoleArea.setForeground(Color.BLACK);
            }
        });

        runButton.addActionListener(e -> {
                    try {
                        runCode();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
        stopButton.addActionListener(e -> stopCode());
        stopButton.setVisible(false);


        fileMenu.add(createNewItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveItemAs);
        menuBar.add(fileMenu);

        colorMenu.add(setBgColorWhite);
        colorMenu.add(setBgColorDark);
        viewMenu.add(colorMenu);
        menuBar.add(viewMenu);

        menuBar.add(runButton);
        menuBar.add(stopButton);

        frame.setJMenuBar(menuBar);
    }

    private void createNewFile() {
        int option = JOptionPane.showConfirmDialog(this, "Czy chcesz zapisać zmiany?");
        if(option != JOptionPane.CANCEL_OPTION) {
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            }
            textArea.setText("");
            currentFile = null;
            setWindowTitle("Nowy plik");
        }
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

    private SaveResult saveFile() {
        if(currentFile != null) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                setWindowTitle(currentFile.getName());
                return SaveResult.SAVED;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            saveFileAs();
        }
        return SaveResult.CANCELLED;
    }

    private SaveResult saveFileAs() {
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            if (!currentFile.getName().endsWith(".java")) {
                currentFile = new File(currentFile.getAbsolutePath() + ".java");
            }
            return saveFile();
        } else {
            return SaveResult.CANCELLED;
        }
    }

    private void setWindowTitle(String fileName) {
        frame.setTitle("Code Editor - " + fileName);
    }

    class Terminator extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
           onClose();
        }
    }

    private void onClose() {
        int option = JOptionPane.showConfirmDialog(this, "Czy chcesz zapisać zmiany przed zamknięciem?");
        if(option == JOptionPane.YES_OPTION) {
            saveFile();
            System.exit(0);
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void runCode() throws IOException {
        consoleArea.setText("");
        SaveResult result = saveFile();
        if (result == SaveResult.CANCELLED || result == SaveResult.FAILED) {
            consoleArea.append("Operacja zapisu nie powiodła się lub została anulowana.\n");
            runButton.setVisible(true);
            stopButton.setVisible(false);
            return;
        }

        String compileFilePath = currentFile.getAbsolutePath();
        String runFilePath = currentFile.getParent();
        runButton.setVisible(false);
        stopButton.setVisible(true);

        try {
            Process compileProcess = new ProcessBuilder("cmd.exe", "/c", "javac", compileFilePath)
                    .directory(currentFile.getParentFile())
                    .start();
            currentProcess = compileProcess;
            compileProcess.waitFor();

            if (compileProcess.exitValue() != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                StringBuilder errorMessage = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorMessage.append(line).append("\n");
                }
                consoleArea.append("Kompilacja nie powiodła się:\n" + errorMessage.toString());
                runButton.setVisible(true);
                stopButton.setVisible(false);
                return;
            }

            String className = currentFile.getName().replace(".java", "");
            Process runProcess = new ProcessBuilder("cmd.exe", "/c", "cd", runFilePath, "&&", "java", className)
                    .start();
            currentProcess = runProcess;
            runThread = new Thread(() -> {
                try {
                    int exitCode = runProcess.waitFor();
                    System.out.println("Proces zakończony z kodem wyjścia: " + exitCode);

                    BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                    BufferedReader stderrReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                    String line;
                    consoleArea.setText("");
                    while ((line = stdoutReader.readLine()) != null) {
                        consoleArea.append(line + "\n");
                    }
                    while ((line = stderrReader.readLine()) != null) {
                        consoleArea.append(line + "\n");
                    }

                } catch (InterruptedException e) {
                    System.out.println("Wątek został przerwany.");
                    consoleArea.append("Proces został zatrzymany.\n");
                    runProcess.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                        runButton.setVisible(true);
                        stopButton.setVisible(false);
                }
            });
            runThread.start();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            runButton.setVisible(true);
            stopButton.setVisible(false);
        }
    }

    private void stopCode() {
        if (currentProcess != null) {
            currentProcess.destroy();
        }
        if (runThread != null && runThread.isAlive()) {
            runThread.interrupt();
        }
        runButton.setVisible(true);
        stopButton.setVisible(false);
    }


    public static void main(String[] args) {
        FlatDarkFlatIJTheme.setup();
        //FlatDarculaLaf.setup();

        try {
            UIManager.setLookAndFeel( new FlatDarkFlatIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize UIManager" );
        }

        MainWindow codeEditor = new MainWindow();
    }
}
