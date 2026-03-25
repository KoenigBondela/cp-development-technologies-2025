package com.hoteldb.labs.jpa;

import com.hoteldb.labs.jpa.service.ClientService;
import com.hoteldb.labs.jpa.service.RoomService;
import com.hoteldb.labs.jpa.service.UniversalRelationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Лабораторная работа №2
 * Вывод универсального отношения базы данных через JPA/Hibernate
 */
public class Lab2Main {
    public static void main(String[] args) {
        RoomService roomService = new RoomService();
        ClientService clientService = new ClientService();
        UniversalRelationService relationService = new UniversalRelationService();

        try {
            outputUniversalRelation(relationService);
        } finally {
            roomService.close();
            clientService.close();
            relationService.close();
        }
    }

    /**
     * Вывод универсального отношения (LEFT JOIN таблиц rooms и clients)
     */
    private static void outputUniversalRelation(UniversalRelationService relationService) {
        List<Object[]> results = relationService.getUniversalRelation();
        
        System.out.println("Room ID | Room Number | Room Type | Price    | Available | " +
                "Client ID | First Name | Last Name  | Email                      | Phone           | Check-In   | Check-Out");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Object[] row : results) {
            Integer roomId = (Integer) row[0];
            String roomNumber = (String) row[1];
            String roomType = (String) row[2];
            BigDecimal price = (BigDecimal) row[3];
            Boolean isAvailable = (Boolean) row[4];
            Integer clientId = (Integer) row[5];
            String firstName = (String) row[6];
            String lastName = (String) row[7];
            String email = (String) row[8];
            String phone = (String) row[9];
            LocalDate checkIn = (LocalDate) row[10];
            LocalDate checkOut = (LocalDate) row[11];

            System.out.printf("%-7d | %-11s | %-9s | %-8.2f | %-9s | %-9s | %-10s | %-10s | %-27s | %-15s | %-10s | %-10s%n",
                    roomId,
                    roomNumber,
                    roomType,
                    price,
                    isAvailable ? "Yes" : "No",
                    clientId != null ? clientId : "NULL",
                    firstName != null ? firstName : "NULL",
                    lastName != null ? lastName : "NULL",
                    email != null ? email : "NULL",
                    phone != null ? phone : "NULL",
                    checkIn != null ? checkIn : "NULL",
                    checkOut != null ? checkOut : "NULL"
            );
        }
    }
}

