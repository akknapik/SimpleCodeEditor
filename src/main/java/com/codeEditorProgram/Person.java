package com.codeEditorProgram;

public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private long ID;

    public Person() {
    }

    public Person(String firstName, String lastName, int age, long ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
