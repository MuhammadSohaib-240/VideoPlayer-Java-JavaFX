package com.example.videoplayer;

import java.sql.*;
import java.util.ArrayList;

public class SQLite3Operations {
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    ArrayList<String> durationList = new ArrayList<>();

    Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:D:\\Software Engineering\\App Development\\Desktop Development\\Video Player - Java - JavaFX\\Video Player\\VideosDatabase.db";

            String sql = """
                    CREATE TABLE IF NOT EXISTS videoLibrary (
                     id integer PRIMARY KEY,
                     videoName text NOT NULL,
                     videoLocation text NOT NULL,
                     videoDuration text NOT NULL);""";

            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String videoName, String videoLocation, String videoDuration) {
        String sql = "INSERT INTO videoLibrary(videoName, videoLocation, videoDuration) VALUES(?,?,?)";

        try {
            Connection conn = this.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, videoName);
            preparedStatement.setString(2, videoLocation);
            preparedStatement.setString(3, videoDuration);
            int length = idList.size() + 1;
            idList.add(length);
            nameList.add(videoName);
            locationList.add(videoLocation);
            durationList.add(videoDuration);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void read() {
        String sql = "SELECT * FROM videoLibrary";

        try {
            Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String videoName = resultSet.getString("videoName");
                String videoLocation = resultSet.getString("videoLocation");
                String videoDuration = resultSet.getString("videoDuration");
                idList.add(id);
                nameList.add(videoName);
                locationList.add(videoLocation);
                durationList.add(videoDuration);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searching(String searchingWord) {
        String sql = "SELECT * FROM videoLibrary WHERE videoName LIKE '%" + searchingWord + "%'";

        try {
            Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String videoName = resultSet.getString("videoName");
                String videoLocation = resultSet.getString("videoLocation");
                String videoDuration = resultSet.getString("videoDuration");
                idList.add(id);
                nameList.add(videoName);
                locationList.add(videoLocation);
                durationList.add(videoDuration);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM videoLibrary WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Set the corresponding param
            preparedStatement.setInt(1, id);
            // Execute the delete statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM videoLibrary";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            idList.clear();
            nameList.clear();
            locationList.clear();
            durationList.clear();
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed");
            System.out.println(e.getMessage());
        }
    }

    public void update(int id, String videoName, String videoLocation, String videoDuration) {
        String sql = "UPDATE videoLibrary SET videoName = ? , "
                + "videoLocation = ? , "
                + "videoDuration = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Set the corresponding param
            preparedStatement.setString(1, videoName);
            preparedStatement.setString(2, videoLocation);
            preparedStatement.setString(3, videoDuration);
            preparedStatement.setInt(4, id);

            // Update
            preparedStatement.executeUpdate();
            read();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
