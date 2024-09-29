package com.example.practica.Controller;

import com.example.practica.DAO.FullName;
import com.example.practica.Service.AuthenticationService;
import com.example.practica.DTO.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    // Визуальные элементы формы
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    // Инициализация кнопки входа
    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }

    // Обработка входа в систему
    private void handleLogin() {
        String username = usernameField.getText();
        FullName fullName = new FullName(username);
        String password = passwordField.getText();

        if (areCredentialsValid(fullName, password)) {
            messageLabel.setText("Успешный вход!");
            User user = new User(username, password, "Photographer");
            AuthenticationService.setCurrentUser(user);
            openPhotograpForm();
        } else {
            messageLabel.setText("Неправильное имя или пароль");
        }
    }

    // Проверка учетных данных пользователя
    private boolean areCredentialsValid(FullName fullName, String password) {
        if (!fullName.isFullNameCorrect()) {
            return false;
        }

        String query = "SELECT 1 FROM doctors WHERE first_name = ? AND last_name = ? AND password = ?";
        return DBConnectionController.searchInDB(query, fullName.getFirstName(), fullName.getLastName(), password);
    }

    // Открытие формы фoтогрофа
    private void openPhotograpForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/practica/photograh_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Форма Фотографа");
            stage.setScene(new Scene(root));
            stage.show();

            // Закрыть текущее окно входа
            loginButton.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleCloseButton(ActionEvent event) {
        Platform.exit(); // Или stage.close(), если вы используете Stage
    }

}
