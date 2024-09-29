    package com.example.practica.Controller;

    import com.example.practica.DTO.ServiceAssignment;
    import com.example.practica.Mapper.ResultSetMapper;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.sql.*;
    import java.time.LocalDate;

    // Класс для управления подключением к базе данных и выполнением запросов.
    public class DBConnectionController {

        // Информация для подключения к базе данных
        private static final String URL = "jdbc:postgresql://localhost:5432/Photostudia";
        private static final String USERNAME = "postgres";
        private static final String PASSWORD = "2426574323";

        private static Connection connection;

        // Получение подключения к базе данных
        public static Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            return connection;
        }

        // Поиск информации в базе данных по запросу
        public static boolean searchInDB(String query, String... args) {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                setPreparedStatementArgs(preparedStatement, args);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Выполнение запроса и возвращение результатов
        public static <T> ObservableList<T> executeQuery(String query, ResultSetMapper<T> mapper, String... args) {
            ObservableList<T> results = FXCollections.observableArrayList();

            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                setPreparedStatementArgs(preparedStatement, args);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        T obj = mapper.map(resultSet);
                        results.add(obj);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return results;
        }

        // Вставка записи в базу данных
        public static boolean insertIntoDB(String query, Object... params) {
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]); // Устанавливаем параметры в запрос
                }

                return pstmt.executeUpdate() > 0; // Возвращает true, если запись добавлена
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Возвращает false в случае ошибки
            }
        }

        // Получение назначений услуг
        public static ObservableList<ServiceAssignment> getServiceAssignments() throws SQLException {
            String query = "SELECT cs.client_id, cs.service_id, c.first_name || ' ' || c.last_name AS client_name, " +
                    "s.service_name, s.description AS service_description, s.price AS service_price, cs.date_of_service " +
                    "FROM client_services cs " +
                    "JOIN clients c ON cs.client_id = c.client_id " +
                    "JOIN medical_services s ON cs.service_id = s.service_id";
            return executeQuery(query, resultSet -> new ServiceAssignment(
                    resultSet.getInt("client_id"),
                    resultSet.getInt("service_id"),
                    resultSet.getString("client_name"),
                    resultSet.getString("service_name"),
                    resultSet.getString("service_description"),
                    resultSet.getDouble("service_price"), // Добавляем цену услуги
                    resultSet.getDate("date_of_service").toLocalDate()
            ));
        }



        public static boolean removeServiceAssignment(int clientId, int serviceId, LocalDate dateOfService) throws SQLException {
            String deleteSQL = "DELETE FROM client_services WHERE client_id = ? AND service_id = ? AND date_of_service = ?";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, clientId);
                preparedStatement.setInt(2, serviceId);
                preparedStatement.setDate(3, Date.valueOf(dateOfService));
                return preparedStatement.executeUpdate() > 0;
            }
        }




        // Установка параметров для PreparedStatement
        private static void setPreparedStatementArgs(PreparedStatement preparedStatement, Object... args) throws SQLException {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    preparedStatement.setString(i + 1, (String) args[i]);
                } else if (args[i] instanceof LocalDate) {
                    preparedStatement.setDate(i + 1, Date.valueOf((LocalDate) args[i]));
                } else if (args[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) args[i]);
                }
            }
        }
        public static void removeServiceAssignment(int id) throws SQLException {
            String deleteSQL = "DELETE FROM client_services WHERE id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        }


    }

