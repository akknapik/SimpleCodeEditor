package com.codeEditorProgram;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CodeEditor codeEditor = new CodeEditor();

        String prefix = "";
        List<String> methodsFromClass = codeEditor.suggestMethods(codeEditor, prefix);

        System.out.println("Public methods: ");
        for(String methodName : methodsFromClass) {
            System.out.println(methodName);
        }
        System.out.println(methodsFromClass.size());
    }
}
