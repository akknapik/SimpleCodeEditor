package com.codeEditorProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CodeEditor codeEditor = new CodeEditor();

        String testString = new String();

        String prefix = "";
        List<String> methodsFromClass = codeEditor.suggestMethods(new HashMap<>(), prefix);

        System.out.println("Public methods: ");
        for(String methodName : methodsFromClass) {
            System.out.println(methodName);
        }
        System.out.println(methodsFromClass.size());
    }
}
