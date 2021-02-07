package SDA.JDBC.AntraUzduotis;

import java.sql.*;

public class PirmaUzuotis {
    private static final String URL = "jdbc:h2:mem:theater";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.h2.Driver"); //pasitikrinam
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            MovieDAO movieDAO = new MovieDAOImpl(conn);
            movieDAO.createTable();

            createMovie("Interstellar", "Animation", 2003, conn);
            createMovie("100", "Animation", 2003, conn);
            createMovie("1001", "Action", 2014, conn);

            System.out.println("Prieš:");
            printMovies(conn);

            try (PreparedStatement updateMovie = conn.prepareStatement("UPDATE MOVIES SET title = ? WHERE id = ?")) {
                updateMovie.setString(1, "A.I.");
                updateMovie.setInt(2, 1);
                updateMovie.execute();
            }

            System.out.println("Po:");
            deleteMovie(2, conn);
            printMovies(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printMovies(Connection conn) throws SQLException {
        try (Statement selectMovies = conn.createStatement()) {
            ResultSet resultSet = selectMovies.executeQuery("SELECT * FROM MOVIES");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int yearOfRelease = resultSet.getInt("yearOfRelease");
                System.out.println("id: " + id + "; title: " + title + "; genre " + genre + "; year of release:" + yearOfRelease);
            }
        }
    }

    private static void createMovie(String title, String genre, int yearOfRelease, Connection conn) {
        try (PreparedStatement insertMovie = conn.prepareStatement("INSERT INTO MOVIES (title, genre, yearOfRelease) VALUES (?, ?, ?)")) {
            insertMovie.setString(1, title);
            insertMovie.setString(2, genre);
            insertMovie.setInt(3, yearOfRelease);
            insertMovie.execute();
        } catch (SQLException e) { //rekomenduojama
            System.out.println(e.getMessage());
        }
    }

    private static void deleteMovie(int id, Connection conn) {
        try (PreparedStatement deleteMovie = conn.prepareStatement("DELETE FROM MOVIES WHERE id = ?")) {
            deleteMovie.setInt(1, id);
            deleteMovie.execute();
        } catch (SQLException e) { //rekomenduojama
            System.out.println(e.getMessage());
        }
    }


}
