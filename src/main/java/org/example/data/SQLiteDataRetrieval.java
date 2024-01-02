package org.example.data;

import org.example.entity.Holiday;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class SQLiteDataRetrieval {
    private static Connection connection;
    private static PreparedStatement preparedStatement;

    {
        openConnectionDB();
    }

    private void openConnectionDB() {
        try (var propertiesFile = SQLiteDataRetrieval.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesFile);
            connection = DriverManager.getConnection(properties.getProperty("db.url"));
            preparedStatement = connection.prepareStatement("SELECT * FROM holidays");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, ?>> getDataAsMaps() {
        List<Map<String, ?>> holidayList = new LinkedList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> holiday = new HashMap<>();
                holiday.put("country", resultSet.getString("country"));
                holiday.put("date", resultSet.getDate("date"));
                holiday.put("name", resultSet.getString("name"));
                holidayList.add(holiday);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return holidayList;
    }

    public List<Holiday> getDataAsEntities() {
        List<Holiday> holidayList = new LinkedList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                holidayList.add(
                        new Holiday(
                                resultSet.getString("country"),
                                resultSet.getDate("date"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return holidayList;
    }

    public ResultSet getDataAsResultSet() {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
