package com.codeEditorProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CodeEditor codeEditor = new CodeEditor();

        String testString = new String();
        Person person = new Person();

        String prefix = "";
        List<String> methodsFromClass = codeEditor.suggestMethods(person, prefix);

        System.out.println("Public methods: ");
        for(String methodName : methodsFromClass) {
            System.out.println(methodName);
        }
        System.out.println(methodsFromClass.size());
    }
}
