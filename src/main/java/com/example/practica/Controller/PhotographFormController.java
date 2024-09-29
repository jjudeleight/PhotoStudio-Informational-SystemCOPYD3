package com.example.practica.Controller;

import com.example.practica.Service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class PhotographFormController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

    @FXML
    private DatePicker stayDurationPicker;

    @FXML
    private Button addPatientButton;

    @FXML
    private Button viewServicesButton;

    @FXML
    private Button assignServiceButton;

    @FXML
    private Button viewMedicamentsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void openClientsView() {
        openNewStage("/com/example/practica/clients_view.fxml", "Просмотр клиентов");
    }

    @FXML
    private void initialize() {
        welcomeLabel.setText("Здравствуйте, " + AuthenticationService.getCurrentUser().getUsername());
        addPatientButton.setOnAction(event -> tryInsertClient());
        viewServicesButton.setOnAction(event -> openServicesView());
        assignServiceButton.setOnAction(event -> openAssignServiceView());
        viewMedicamentsButton.setOnAction(event -> openMedicineView());
        logoutButton.setOnAction(event -> logout());

    }

    private void tryInsertClient() {
        if (insertClient()) {
            showAlert("Успех", "Клиент успешно добавлен!", Alert.AlertType.INFORMATION);
        }
    }

    private boolean insertClient() {
        if (!areFieldsValid()) {
            showAlert("Ошибка", "Все поля должны быть заполнены", Alert.AlertType.ERROR);
            return false;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        LocalDate stayDuration = stayDurationPicker.getValue();

        String insertSQL = "INSERT INTO clients (first_name, last_name, address, stay_duration, phone_number) VALUES (?, ?, ?, ?, ?)";
        return DBConnectionController.insertIntoDB(insertSQL, firstName, lastName, address, stayDuration, phone);
    }

    private boolean areFieldsValid() {
        return !firstNameField.getText().isEmpty()
                && !lastNameField.getText().isEmpty()
                && !addressField.getText().isEmpty()
                && !phoneField.getText().isEmpty()
                && stayDurationPicker.getValue() != null;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openServicesView() {
        openNewStage("/com/example/practica/services_view.fxml", "Услуги фотостудии");
    }

    private void openAssignServiceView() {
        openNewStage("/com/example/practica/assign_service_view.fxml", "Добавить оборудование к фотосессии");
    }

    private void openMedicineView() {
        openNewStage("/com/example/practica/studiafoto_form.fxml", "Все оборудование");
    }

    private void openNewStage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        AuthenticationService.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/practica/login_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Логин");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
