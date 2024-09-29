package com.example.practica.DTO;

import java.time.LocalDate;

public class Client {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private LocalDate stayDuration; // Измените тип на LocalDate, если это нужно

    public Client(String firstName, String lastName, String address, LocalDate stayDuration, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.stayDuration = stayDuration;
        this.phoneNumber = phoneNumber;
    }

    // Конструктор, геттеры и сеттеры

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getStayDuration() { // Убедитесь, что это публичный геттер
        return stayDuration;
    }

    public void setStayDuration(LocalDate stayDuration) {
        this.stayDuration = stayDuration;
    }
}
