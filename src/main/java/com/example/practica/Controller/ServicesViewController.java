package com.example.practica.Controller;

import com.example.practica.DTO.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ServicesViewController {
    // Визуальные элементы формы.
    @FXML
    private TableView<Service> servicesTable;

    @FXML
    private TableColumn<Service, Integer> idColumn;

    @FXML
    private TableColumn<Service, String> nameColumn;

    @FXML
    private TableColumn<Service, String> descriptionColumn;

    @FXML
    private TableColumn<Service, BigDecimal> priceColumn;

    @FXML
    private TextField nameField;          // Поле для имени услуги
    @FXML
    private TextField descriptionField;   // Поле для описания услуги
    @FXML
    private TextField priceField;         // Поле для цены услуги
    @FXML

    private TextField idField;
    @FXML
    private Button addButton;             // Кнопка добавления новой услуги

    // Список услуг, найденных в базе данных.
    private ObservableList<Service> serviceList;

    @FXML
    private void initialize() {
        serviceList = FXCollections.observableArrayList();

        // Установка полей для всех столбцов в таблице.
        setupTableColumns();

        // Загружает все услуги из БД в таблицу.
        loadServicesFromDatabase();

        // Обработчик для добавления новой услуги
        addButton.setOnAction(event -> {
            try {
                handleAddService();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadServicesFromDatabase() {
        String query = "SELECT service_id, service_name, description, price FROM medical_services";

        serviceList = DBConnectionController.executeQuery(query, resultSet -> new Service(resultSet));

        servicesTable.setItems(serviceList);
    }


    private void handleAddService() throws SQLException {
        String name = nameField.getText();
        String description = descriptionField.getText();
        BigDecimal price;
        Integer id;

        // Проверяем, заполнены ли поля
        if (name.isEmpty() || description.isEmpty() || priceField.getText().isEmpty() || idField.getText().isEmpty()) {
            showAlert("Ошибка", "Все поля должны быть заполнены.", Alert.AlertType.ERROR);
            return;
        }

        try {
            price = new BigDecimal(priceField.getText());
            id = Integer.parseInt(idField.getText()); // Получаем ID
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Цена и ID должны быть числом.", Alert.AlertType.ERROR);
            return;
        }

        // SQL-запрос для вставки новой услуги с ID
        String insertSQL = "INSERT INTO medical_services (service_id, service_name, description, price) VALUES (?, ?, ?, ?)";

        // Убедитесь, что вы передаете все параметры
        if (DBConnectionController.insertIntoDB(insertSQL, id, name, description, price)) {
            showAlert("Успех", "Услуга успешно добавлена!", Alert.AlertType.INFORMATION);
            clearFields();
            loadServicesFromDatabase(); // Обновляем таблицу
        } else {
            showAlert("Ошибка", "Не удалось добавить услугу", Alert.AlertType.ERROR);
        }
    }



    private void clearFields() {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
