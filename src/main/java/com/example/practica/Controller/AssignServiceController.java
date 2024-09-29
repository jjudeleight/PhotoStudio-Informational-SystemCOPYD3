package com.example.practica.Controller;

import com.example.practica.DAO.FullName;
import com.example.practica.DTO.ServiceAssignment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class AssignServiceController {
    @FXML
    private TableView<ServiceAssignment> servicesTable;

    @FXML
    private TableColumn<ServiceAssignment, String> clientNameColumn;

    @FXML
    private TableColumn<ServiceAssignment, String> serviceNameColumn;

    @FXML
    private TableColumn<ServiceAssignment, LocalDate> dateOfServiceColumn;

    @FXML
    private TextField patientNameField;

    @FXML
    private TextField serviceNameField;

    @FXML
    private DatePicker serviceDatePicker;

    @FXML
    private TableColumn<ServiceAssignment, String> serviceDescriptionColumn;

    @FXML
    private TableColumn<ServiceAssignment, Double> servicePriceColumn;


    @FXML
    private Button assignServiceButton;

    @FXML
    private void initialize() {
        // Устанавливаем фабрики для колонок
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        serviceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("serviceDescription"));
        servicePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price")); // Устанавливаем фабрику для нового столбца
        dateOfServiceColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfService"));

        loadData(); // Загружаем данные сразу после инициализации

        assignServiceButton.setOnAction(event -> handleAssignService());
    }



    private void loadData() {
        try {
            ObservableList<ServiceAssignment> assignments = DBConnectionController.getServiceAssignments();
            servicesTable.setItems(assignments);
        } catch (SQLException e) {
            showAlert("Ошибка", "Не удалось загрузить данные: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleAssignService() {
        try {
            if (assignServiceToPatient()) {
                showAlert("Успех", "Услуга успешно добавлена!", Alert.AlertType.INFORMATION);
                clearFields();
                refreshTable(); // Обновляем таблицу после добавления услуги
            } else {
                showAlert("Ошибка", "Не все поля заполнены", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Ошибка", "Ошибка при добавлении услуги: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean assignServiceToPatient() throws SQLException {
        String patientName = patientNameField.getText();
        String serviceName = serviceNameField.getText();
        LocalDate serviceDate = serviceDatePicker.getValue();

        if (patientName.isEmpty() || serviceName.isEmpty() || serviceDate == null) {
            return false;
        }

        Integer serviceId = findServiceId(serviceName);
        if (serviceId == null) return false;

        Integer clientId = findClientId(new FullName(patientName));
        if (clientId == null) return false;

        String assignSQL = "INSERT INTO client_services (client_id, service_id, date_of_service) VALUES (?, ?, ?)";
        return DBConnectionController.insertIntoDB(assignSQL, clientId, serviceId, serviceDate);
    }

    private Integer findClientId(FullName patientFullName) throws SQLException {
        String query = "SELECT client_id FROM clients WHERE first_name = ? AND last_name = ?";
        return DBConnectionController.executeQuery(query,
                resultSet -> resultSet.getInt("client_id"),
                patientFullName.getFirstName(),
                patientFullName.getLastName()
        ).stream().findFirst().orElse(null);
    }

    private Integer findServiceId(String serviceName) throws SQLException {
        String query = "SELECT service_id FROM medical_services WHERE service_name = ?";
        return DBConnectionController.executeQuery(query,
                resultSet -> resultSet.getInt("service_id"),
                serviceName
        ).stream().findFirst().orElse(null);
    }

    private void refreshTable() {
        loadData(); // Обновляем содержимое таблицы
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        patientNameField.clear();
        serviceNameField.clear();
        serviceDatePicker.setValue(null);
    }

    @FXML
    private void handleRemoveService() {
        ServiceAssignment selectedService = servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            try {
                boolean deleted = DBConnectionController.removeServiceAssignment(
                        selectedService.getClientId(),
                        selectedService.getServiceId(),
                        selectedService.getDateOfService()
                );

                if (deleted) {
                    showAlert("Успех", "Услуга успешно удалена!", Alert.AlertType.INFORMATION);
                    loadData(); // Обновляем таблицу после удаления услуги
                } else {
                    showAlert("Ошибка", "Не удалось удалить услугу.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                showAlert("Ошибка", "Ошибка при удалении услуги: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите услугу для удаления.", Alert.AlertType.ERROR);
        }
    }


}



