package com.hoteldb.labs.aspectj;

public aspect AspectManners {
    
    pointcut sayMethod(): execution(* HelloWorld.say*(..));
    pointcut methodsWithoutSay(): execution(* HelloWorld.*(..)) && !sayMethod();

    before(): methodsWithoutSay() {
        System.out.println("Entering method without say " + thisJoinPoint + " Timestamp:" + 
            System.currentTimeMillis());
    }

    after(): methodsWithoutSay() {
        System.out.println("Leaving method without say " + thisJoinPoint + " Timestamp:" + 
            System.currentTimeMillis());
    }

    pointcut callSayMessageToPerson(String person) : call(* HelloWorld.sayToPerson(String, String)) && args(*, person);

    void around(String person): callSayMessageToPerson(person) {
        proceed(person + "-san");
    }

}

