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
            //movieDAO.deleteTable();

            movieDAO.createMovie(new Movie("Interstelar", "Action", 2000));
            movieDAO.createMovie(new Movie("100", "Animation", 2003));
            movieDAO.createMovie(new Movie("1001", "Action", 2014));

            System.out.println("Prieš:");
            movieDAO.findAll().forEach(System.out::println);
            movieDAO.updateMoviesTitle(1, "A.I");

            System.out.println("Po:");
            movieDAO.deleteMovie(2);
            movieDAO.findAll().forEach(System.out::println);

            System.out.println(movieDAO.findMovieById(3).get());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
