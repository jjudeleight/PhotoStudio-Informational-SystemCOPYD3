package com.example.practica.Controller;

import com.example.practica.DTO.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientsViewController {
    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, String> firstNameColumn;

    @FXML
    private TableColumn<Client, String> lastNameColumn;

    @FXML
    private TableColumn<Client, String> addressColumn;

    @FXML
    private TableColumn<Client, String> phoneNumber; // Изменил имя переменной для ясности

    @FXML
    private TableColumn<Client, LocalDate> stayDuration; // Изменил тип на LocalDate

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        stayDuration.setCellValueFactory(new PropertyValueFactory<>("stayDuration"));

        loadClients();
    }


    private void loadClients() {
        String url = "jdbc:postgresql://localhost:5432/Sanatorium"; // Ваш URL
        String user = "postgres"; // Ваш логин
        String password = "2426574323"; // Ваш пароль

        String query = "SELECT first_name, last_name, address, stay_duration, phone_number FROM clients";

        List<Client> clients = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                LocalDate stayDuration = resultSet.getDate("stay_duration").toLocalDate();
                String phoneNumber = resultSet.getString("phone_number");

                // Добавление клиента в список
                clients.add(new Client(firstName, lastName, address, stayDuration, phoneNumber));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Установка данных в таблицу
        clientsTable.setItems(FXCollections.observableArrayList(clients));
    }
}
