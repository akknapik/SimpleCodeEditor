package com.codeEditorProgram;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CodeEditor {
    public List<String> suggestMethods(Object object, String prefix) {
        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getName)
                .filter(methodName -> methodName.startsWith(prefix))
                .distinct()
                .collect(Collectors.toList());
    }
}
