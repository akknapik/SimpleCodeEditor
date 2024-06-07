package com.codeEditorProgram;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.Assert.*;

@DisplayName("Code editor validation test")
public class CodeEditorTest {

    private CodeEditor codeEditor;
    @Before
    public void setup() {
        codeEditor = new CodeEditor();
    }


    @Test
    public void givenClassHashMap_whenGetsAllPublicMethods_thenCorrect() {
        // Given
        HashMap testHashmap = new HashMap<>();
        String prefix = "";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testHashmap, prefix);

        // Then
        assertEquals("HashMap", testHashmap.getClass().getSimpleName());
        assertEquals(23, methodNames.size());
    }

    @Test
    public void givenClassHashMap_whenGetsSpecificPublicMethods_thenCorrect() {
        // Given
        HashMap testHashmap = new HashMap<>();
        String prefix = "c";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testHashmap, prefix);

        // Then
        assertEquals("HashMap", testHashmap.getClass().getSimpleName());
        assertEquals(7, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("clone", "clear", "compute", "containsKey",
                "computeIfAbsent", "containsValue", "computeIfPresent")));
        assertFalse(methodNames.contains("replace"));

    }

    @Test
    public void givenClassString_whenGetsAllPublicMethods_thenCorrect() {
        // Given
        String testString = new String();
        String prefix = "";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testString, prefix);

        // Then
        assertEquals("String", testString.getClass().getSimpleName());
        assertEquals(54, methodNames.size());
    }

    @Test
    public void givenClassString_whenGetsSpecificPublicMethods_thenCorrect() {
        // Given
        String testString = new String();
        String prefix = "i";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testString, prefix);

        // Then
        assertEquals("String", testString.getClass().getSimpleName());
        assertEquals(5, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("indexOf", "isEmpty", "isBlank", "indent",
                "intern")));
        assertFalse(methodNames.contains("contains"));
    }

    @Test
    public void givenClassArrayList_whenGetsSpecificPublicMethods_thenCorrect() {
        // Given
        ArrayList testArrayList = new ArrayList<>();
        String prefix = "add";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testArrayList, prefix);

        // Then
        assertEquals("ArrayList", testArrayList.getClass().getSimpleName());
        assertEquals(2, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("add", "addAll")));
        assertFalse(methodNames.contains("indexOf"));
    }

    @Test
    public void givenClassScanner_whenGetsAllPublicMethods_thenCorrect() {
        // Given
        Scanner testScanner = new Scanner(System.in);
        String prefix = "";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testScanner, prefix);

        // Then
        assertEquals("Scanner", testScanner.getClass().getSimpleName());
        assertEquals(39, methodNames.size());
    }

    @Test
    public void givenClassScanner_whenGetsSpecificPublicMethods_thenCorrect() {
        // Given
        Scanner testScanner = new Scanner(System.in);
        String prefix = "next";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testScanner, prefix);

        // Then
        assertEquals("Scanner", testScanner.getClass().getSimpleName());
        assertEquals(11, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("next", "nextDouble", "nextInt", "nextLong", "nextBoolean",
                "nextFloat", "nextLine", "nextByte", "nextShort", "nextBigInteger", "nextBigDecimal")));
        assertFalse(methodNames.contains("hasNextByte"));
    }

    @Test
    public void givenClassPerson_whenGetsAllPublicMethods_thenCorrect() {
        // Given
        Person testPerson = new Person();
        String prefix = "";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testPerson, prefix);

        // Then
        assertEquals("Person", testPerson.getClass().getSimpleName());
        assertEquals(6, methodNames.size());
    }

    @Test
    public void givenClassPerson_whenGetsSpecificPublicMethods_thenCorrect() {
        // Given
        Person testPerson = new Person();
        String prefix = "get";

        // When
        List<String> methodNames = codeEditor.suggestMethods(testPerson, prefix);

        // Then
        assertEquals("Person", testPerson.getClass().getSimpleName());
        assertEquals(3, methodNames.size());
        assertTrue(methodNames.containsAll(Arrays.asList("getLastName", "getFirstName", "getAge")));
        assertFalse(methodNames.contains("setAge"));
    }
}