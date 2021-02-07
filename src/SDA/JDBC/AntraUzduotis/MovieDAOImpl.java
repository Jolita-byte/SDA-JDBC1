package SDA.JDBC.AntraUzduotis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {
    private final Connection connection;

    public MovieDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS MOVIES (" +
                    "id INTEGER AUTO_INCREMENT, " +
                    "title VARCHAR(255)," +
                    "genre VARCHAR(255)," +
                    "yearOfRelease INTEGER" +
                    ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS MOVIES");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void createMovie(Movie movie) {
        try (PreparedStatement insertMovie = connection.prepareStatement("INSERT INTO MOVIES (title, genre, yearOfRelease) VALUES (?, ?, ?)")) {
            insertMovie.setString(1, movie.getTitle());
            insertMovie.setString(2, movie.getGenre());
            insertMovie.setInt(3, movie.getYearOfRelease());
            insertMovie.execute();
        } catch (SQLException e) { //rekomenduojama
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteMovie(int id) {
        try (PreparedStatement deleteMovie = connection.prepareStatement("DELETE FROM MOVIES WHERE id = ?")) {
            deleteMovie.setInt(1, id);
            deleteMovie.execute();
        } catch (SQLException e) { //rekomenduojama
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateMoviesTitle(int id, String newTitle) {
        try (PreparedStatement updateMovie = connection.prepareStatement("UPDATE MOVIES SET title = ? WHERE id = ?")) {
            updateMovie.setString(1, newTitle);
            updateMovie.setInt(2, 1);
            updateMovie.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Movie> findMovieById(int id) {
        try (PreparedStatement findMovie = connection.prepareStatement("SELECT * FROM MOVIES WHERE ID = ?")){
            findMovie.setInt(1, id);
            if (!findMovie.execute()){
                return Optional.empty();
            }

            ResultSet resultSet = findMovie.getResultSet();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int yearOfRelease = resultSet.getInt("yearOfRelease");
                return Optional.of(new Movie(id, title, genre, yearOfRelease));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movieList = new ArrayList<>();
        try (Statement selectMovies = connection.createStatement()) {
            ResultSet resultSet = selectMovies.executeQuery("SELECT * FROM MOVIES");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int yearOfRelease = resultSet.getInt("yearOfRelease");
                movieList.add(new Movie(id, title, genre, yearOfRelease));
                //System.out.println("id: " + id + "; title: " + title + "; genre " + genre + "; year of release:" + yearOfRelease);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return movieList;
    }
}
