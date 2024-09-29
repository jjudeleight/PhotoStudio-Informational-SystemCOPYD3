package com.example.practica.Controller;

import com.example.practica.DTO.Photo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class StudioOpisanieController {
    @FXML
    private TableView<Photo> PhotoTabel;

    @FXML
    private TableColumn<Photo, Integer> medicament_id;

    @FXML
    private TableColumn<Photo, String> nameStudiaColumn;

    @FXML
    private TableColumn<Photo, String> OpisanieStudiiColumn;

    @FXML
    private TableColumn<Photo, String> FormaFotossesiiColumn;

    @FXML
    private TableColumn<Photo, String> countryColumn;

    @FXML
    private TableColumn<Photo, String> CallChasovColumn;

    @FXML
    private TableColumn<Photo, String> Coli4estvoFotoColumn;

    private ObservableList<Photo> PhotoList;

    @FXML
    private void initialize() {
        medicament_id.setCellValueFactory(new PropertyValueFactory<>("medicament_Id"));
        nameStudiaColumn.setCellValueFactory(new PropertyValueFactory<>("nameStudiaColumn"));
        OpisanieStudiiColumn.setCellValueFactory(new PropertyValueFactory<>("OpisanieStudiiColumn"));
        FormaFotossesiiColumn.setCellValueFactory(new PropertyValueFactory<>("FormaFotossesiiColumn"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryColumn"));
        CallChasovColumn.setCellValueFactory(new PropertyValueFactory<>("callChasovColumn"));
        Coli4estvoFotoColumn.setCellValueFactory(new PropertyValueFactory<>("Coli4estvoFotoColumn"));

        // Загружаем данные из базы данных...
        loadPhotoFromDatabase();
    }

    @FXML
    public void handleAddPhoto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить медикамент");
        dialog.setHeaderText("Введите данные медикамента через запятую");
        dialog.setContentText("Формат: medicament_id, nameStudiaColumn, OpisanieStudiiColumn, FormaFotossesiiColumn, countryColumn, CallChasovColumn, Coli4estvoFotoColumn");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(data -> {
            String[] fields = data.split(",");
            if (fields.length == 7) {
                try {
                    int medicament_id = Integer.parseInt(fields[0].trim());
                    String nameStudiaColumn = fields[1].trim();
                    String OpisanieStudiiColumn = fields[2].trim();
                    String FormaFotossesiiColumn = fields[3].trim();
                    String countryColumn = fields[4].trim();
                    String CallChasovColumn = fields[5].trim();
                    String Coli4estvoFotoColumn = fields[6].trim();

                    // Создаем новый объект Photo
                    Photo newPhoto = new Photo(medicament_id, nameStudiaColumn, OpisanieStudiiColumn, FormaFotossesiiColumn, countryColumn, CallChasovColumn, Coli4estvoFotoColumn);

                    // Добавляем медикамент в базу данных
                    addPhotoToDatabase(newPhoto);

                    // Обновляем таблицу
                    loadPhotoFromDatabase();

                    // Уведомление об успешном добавлении
                    showAlert("Успех", "Медикамент успешно добавлен.", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "medicament_id должен быть числом.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Ошибка", "Неверный формат ввода. Пожалуйста, используйте формат: medicament_id, nameStudiaColumn, OpisanieStudiiColumn, FormaFotossesiiColumn, countryColumn, CallChasovColumn, Coli4estvoFotoColumn.", Alert.AlertType.ERROR);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadPhotoFromDatabase() {
        String query = "SELECT * FROM medicaments";
        PhotoList = DBConnectionController.executeQuery(query, resultSet -> new Photo(resultSet));
        PhotoTabel.setItems(PhotoList);
        PhotoTabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void addPhotoToDatabase(Photo medicine) {
        String url = "jdbc:postgresql://localhost:5432/Sanatorium"; // Ваш URL
        String user = "postgres"; // Ваш логин
        String password = "2426574323"; // Ваш пароль

        // Используйте правильные имена столбцов с учетом регистра
        String query = "INSERT INTO medicaments (medicament_id, \"nameStudiaColumn\", \"OpisanieStudiiColumn\", \"FormaFotossesiiColumn\", \"countryColumn\", \"CallChasovColumn\", \"Coli4estvoFotoColumn\") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, medicine.getMedicament_Id());
            preparedStatement.setString(2, medicine.getNameStudiaColumn());
            preparedStatement.setString(3, medicine.getOpisanieStudiiColumn());
            preparedStatement.setString(4, medicine.getFormaFotossesiiColumn());
            preparedStatement.setString(5, medicine.getCountryColumn());
            preparedStatement.setString(6, medicine.getCallChasovColumn());
            preparedStatement.setString(7, medicine.getColi4estvoFotoColumn());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка ошибок
            showAlert("Ошибка", "Не удалось добавить медикамент в базу данных.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeletePhoto() {
        Photo selectedPhoto = PhotoTabel.getSelectionModel().getSelectedItem();

        if (selectedPhoto != null) {
            // Запрос на подтверждение удаления
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления");
            alert.setHeaderText("Вы уверены, что хотите удалить выбранный медикамент?");
            alert.setContentText("Это действие нельзя отменить.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Удаляем запись из базы данных
                deletePhotoFromDatabase(selectedPhoto);
                // Удаляем из списка и обновляем таблицу
                PhotoList.remove(selectedPhoto);
                PhotoTabel.setItems(PhotoList);
                showAlert("Успех", "Медикамент успешно удален.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Ошибка", "Пожалуйста, выберите медикамент для удаления.", Alert.AlertType.ERROR);
        }
    }

    private void deletePhotoFromDatabase(Photo photo) {
        String url = "jdbc:postgresql://localhost:5432/Sanatorium"; // Ваш URL
        String user = "postgres"; // Ваш логин
        String password = "2426574323"; // Ваш пароль

        String query = "DELETE FROM medicaments WHERE medicament_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Photo.getMedicament_Id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка ошибок
            showAlert("Ошибка", "Не удалось удалить медикамент из базы данных.", Alert.AlertType.ERROR);
        }
    }
}
