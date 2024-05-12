package com.codeEditorProgram;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CodeEditorTest {
    CodeEditor codeEditor = new CodeEditor();

    @Test
    public void givenClassHashMap_whenGetsAllPublicMethods_thenCorrect() {
        HashMap testHashmap = new HashMap<>();
        String prefix = "";
        List<String> methodNames = codeEditor.suggestMethods(testHashmap, prefix);

        assertEquals("HashMap", testHashmap.getClass().getSimpleName());
        assertEquals(23, methodNames.size());
    }

    @Test
    public void givenClassHashMap_whenGetsSpecificPublicMethods_thenCorrect() {
        HashMap testHashmap = new HashMap<>();
        String prefix = "c";
        List<String> methodNames = codeEditor.suggestMethods(testHashmap, prefix);

        assertEquals("HashMap", testHashmap.getClass().getSimpleName());
        assertEquals(7, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("clone", "clear", "compute", "containsKey",
                "computeIfAbsent", "containsValue", "computeIfPresent")));
        assertFalse(methodNames.contains("replace"));

    }

    @Test
    public void givenClassString_whenGetsAllPublicMethods_thenCorrect() {
        String testString = new String();
        String prefix = "";
        List<String> methodNames = codeEditor.suggestMethods(testString, prefix);

        assertEquals("String", testString.getClass().getSimpleName());
        assertEquals(54, methodNames.size());
    }

    @Test
    public void givenClassString_whenGetsSpecificPublicMethods_thenCorrect() {
        String testString = new String();
        String prefix = "i";
        List<String> methodNames = codeEditor.suggestMethods(testString, prefix);

        assertEquals("String", testString.getClass().getSimpleName());
        assertEquals(5, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("indexOf", "isEmpty", "isBlank", "indent",
                "intern")));
        assertFalse(methodNames.contains("contains"));
    }

    @Test
    public void givenClassArrayList_whenGetsSpecificPublicMethods_thenCorrect() {
        ArrayList testArrayList = new ArrayList<>();
        String prefix = "add";
        List<String> methodNames = codeEditor.suggestMethods(testArrayList, prefix);

        assertEquals("ArrayList", testArrayList.getClass().getSimpleName());
        assertEquals(2, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("add", "addAll")));
        assertFalse(methodNames.contains("indexOf"));
    }

    @Test
    public void givenClassScanner_whenGetsAllPublicMethods_thenCorrect() {
        Scanner testScanner = new Scanner(System.in);
        String prefix = "";
        List<String> methodNames = codeEditor.suggestMethods(testScanner, prefix);

        assertEquals("Scanner", testScanner.getClass().getSimpleName());
        assertEquals(39, methodNames.size());
    }

    @Test
    public void givenClassScanner_whenGetsSpecificPublicMethods_thenCorrect() {
        Scanner testScanner = new Scanner(System.in);
        String prefix = "next";
        List<String> methodNames = codeEditor.suggestMethods(testScanner, prefix);

        assertEquals("Scanner", testScanner.getClass().getSimpleName());
        assertEquals(11, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("next", "nextDouble", "nextInt", "nextLong", "nextBoolean",
                "nextFloat", "nextLine", "nextByte", "nextShort", "nextBigInteger", "nextBigDecimal")));
        assertFalse(methodNames.contains("hasNextByte"));
    }
}