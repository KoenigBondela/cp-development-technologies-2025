package com.hoteldb.labs.aspectj;

public class HelloWorld {

    private String Name;

    private String FamilyName;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamilyName() {
        return FamilyName;
    }

    public void setFamilyName(String familyName) {
        FamilyName = familyName;
    }

    public void say(String message) {
        System.out.println(message);
    }

    public void sayToPerson(String message, String name) {
        System.out.println(name + ", " + message);
    }
}

