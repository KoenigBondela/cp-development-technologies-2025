package com.hoteldb.labs.aspectj;

public aspect AspectManners {
    
    // Базовые срезы из примера
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

    // Японская вежливость и добавление фамилии - добавляем фамилию и "-san" к имени
    pointcut callSayMessageToPerson(String message, String person): 
        call(* HelloWorld.sayToPerson(String, String)) && args(message, person);

    void around(String message, String person): callSayMessageToPerson(message, person) {
        // Получаем объект HelloWorld из текущего контекста
        HelloWorld target = (HelloWorld) thisJoinPoint.getTarget();
        String fullName = person;
        // Задание 1.4: Добавляем фамилию после имени
        if (target != null && target.getFamilyName() != null) {
            fullName = person + " " + target.getFamilyName();
        }
        // Японская вежливость - добавляем "-san"
        proceed(message, fullName + "-san");
    }

    // Задание 1.1: Логгирование геттеров и сеттеров
    pointcut getterMethods(): execution(* HelloWorld.get*(..));
    pointcut setterMethods(): execution(* HelloWorld.set*(..));

    after() returning: getterMethods() {
        String methodName = thisJoinPoint.getSignature().getName();
        String fieldName = methodName.substring(3); // Убираем "get"
        System.out.println(fieldName + " was getted. " + thisJoinPoint + " Timestamp:" + 
            System.currentTimeMillis());
    }

    after(): setterMethods() {
        String methodName = thisJoinPoint.getSignature().getName();
        String fieldName = methodName.substring(3); // Убираем "set"
        System.out.println(fieldName + " was setted. " + thisJoinPoint + " Timestamp:" + 
            System.currentTimeMillis());
    }

    // Задание 1.2: Вывод "Good day!" перед методами с "say" и "Nice to meet you!" после
    // Используем execution для перехвата выполнения методов say
    before(): sayMethod() {
        System.out.println("Good day!");
    }

    after(): sayMethod() {
        System.out.println("Nice to meet you!");
    }

    // Задание 1.3: После каждого вызова (call) метода, не содержащего "say", выводить сообщение
    // Исключаем конструкторы - только методы
    pointcut callMethodsWithoutSay(): call(* HelloWorld.*(..)) && !call(* HelloWorld.say*(..)) && !call(HelloWorld.new(..));

    after(): callMethodsWithoutSay() {
        System.out.println("Without say method is called");
    }

}

