package com.codeEditorProgram;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private JPopupMenu popupMenu;

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

        Font consoleFont = new Font("Monospaced", Font.PLAIN, 14);
        textArea.setFont(consoleFont);
        consoleArea.setFont(consoleFont);

        frame.add(splitPane);
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        createMenu(frame);
        var listener = new Terminator();
        frame.addWindowListener(listener);

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown()) {
                    List<String> methods;
                    String text = textArea.getText();
                    try {
                        methods = showMethods(text);
                        if (!methods.isEmpty()) {
                            popupMenu = new JPopupMenu();
                            for (int i = 0; i < methods.size(); i++) {
                                JMenuItem menuItem = new JMenuItem(methods.get(i));
                                popupMenu.add(menuItem);
                            }

                            JScrollPane scrollPane = new JScrollPane(popupMenu);
                            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                            int caretPosition = textArea.getCaretPosition();
                            Rectangle rectangle = textArea.modelToView(caretPosition);
                            if (rectangle != null) {
                                popupMenu.show(textArea, rectangle.x, rectangle.y);
                            }
                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

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
        setBgColorDark.addActionListener(e -> {
                textArea.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.WHITE);
                consoleArea.setBackground(Color.DARK_GRAY);
                consoleArea.setForeground(Color.WHITE);
        });

        JMenuItem setBgColorWhite = new JMenuItem("Jasny motyw");
        setBgColorWhite.addActionListener(e -> {
                textArea.setBackground(Color.WHITE);
                textArea.setForeground(Color.BLACK);
                consoleArea.setBackground(Color.WHITE);
                consoleArea.setForeground(Color.BLACK);
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
            consoleArea.setText("");
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
                consoleArea.append("Kompilacja nie powiodła się:\n" + errorMessage);
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

    private List<String> showMethods(String text) throws BadLocationException {
        List<String> packages = Arrays.asList("java.lang.","java.util.","","java.io.","java.nio.","java.net.","java.awt.",
                "javax.swing.","java.sql.","java.text.","java.time.","java.beans.","java.math.","java.rmi.","java.security.",
                "java.util.concurrent.","java.util.function.","java.util.stream.","javax.annotation.","javax.naming.",
                "javax.management.","javax.xml."
        );

        String regex = "([A-Z][a-zA-Z0-9\\[\\]]*(?:<[a-zA-Z, ]*>)?)\\s+([a-zA-Z][a-zA-Z0-9]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        HashMap<String, Object> methods = new HashMap<>();

        while (matcher.find()) {
            String objectType = matcher.group(1).replaceAll("[\\[\\]<>]", "");
            String variableName = matcher.group(2).replaceAll("[\\[\\]<>]", "");

            for (String pkg : packages) {
                try {
                    String fullClassName = pkg + objectType;
                    Class<?> clazz = Class.forName(fullClassName);
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    Object instance = constructor.newInstance();
                    if (instance != null) {
                        methods.put(variableName, instance);
                    }
                    break;
                }  catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                          InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    continue;
                }
            }
        }
        methods.put("System.out",System.out);
        methods.put("System.err",System.err);
        methods.put("System.in",System.in);

            int caretPosition = textArea.getCaretPosition();
            text = textArea.getText(0, caretPosition);

            int dotIndex = text.lastIndexOf('.');

            char[] spaceAndNewLine = new char[] {' ', '\n'};
            int dotIndex2 = text.lastIndexOf('.');
            int spaceOrNewlineIndex = -1;
            for (char c : spaceAndNewLine) {
                int index = text.lastIndexOf(c, dotIndex2);
                if (index > spaceOrNewlineIndex) {
                    spaceOrNewlineIndex = index;
                }
            }

            String objectName = text.substring(spaceOrNewlineIndex + 1, dotIndex).trim();
            String prefix = text.substring(dotIndex + 1).trim();

            Object value = methods.get(objectName);

            CodeEditor codeEditor = new CodeEditor();
            List<String> methodSuggestions = codeEditor.suggestMethods(value, prefix);

            return methodSuggestions;
        }
}