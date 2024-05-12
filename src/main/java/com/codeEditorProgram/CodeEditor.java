package com.codeEditorProgram;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class CodeEditor {
    public List<String> suggestMethods(Object object, String prefix) {
        List<String> suggestedMethods = new ArrayList<>();

        Method[] methods = object.getClass().getDeclaredMethods();

        for(Method method : methods) {
            if(Modifier.isPublic(method.getModifiers())) {
                String methodName = method.getName();
                if(methodName.startsWith(prefix)) {
                    if(!suggestedMethods.contains(methodName)) {
                        suggestedMethods.add(methodName);
                    }
                }
            }
        }

        return suggestedMethods;
    }
}
