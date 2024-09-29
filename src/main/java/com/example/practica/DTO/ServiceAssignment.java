package com.example.practica.DTO;

import java.time.LocalDate;

public class ServiceAssignment {
    private Integer clientId;
    private Integer serviceId;
    private String clientName;
    private String serviceName;
    private String serviceDescription;
    private double price; // Новое поле для цены услуги
    private LocalDate dateOfService;

    public ServiceAssignment(Integer clientId, Integer serviceId, String clientName, String serviceName, String serviceDescription, double price, LocalDate dateOfService) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.clientName = clientName;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.price = price; // Инициализация цены
        this.dateOfService = dateOfService;
    }

    // Геттеры
    public Integer getClientId() { return clientId; }
    public Integer getServiceId() { return serviceId; }
    public String getClientName() { return clientName; }
    public String getServiceName() { return serviceName; }
    public String getServiceDescription() { return serviceDescription; }
    public double getPrice() { return price; } // Геттер для цены
    public LocalDate getDateOfService() { return dateOfService; }
}
