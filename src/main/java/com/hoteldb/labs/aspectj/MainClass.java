package com.hoteldb.labs.aspectj;

public class MainClass {

    public static void main(String[] args) {
        HelloWorld hello = new HelloWorld();
        hello.say("How do you do?");
        hello.setName("John");
        hello.setFamilyName("Jackson");
        hello.sayToPerson("how do you do?", hello.getName());
    }
}

