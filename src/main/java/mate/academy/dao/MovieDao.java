package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Movie;

public interface MovieDao {
    Movie add(Movie movie);

    Optional<Movie> get(Long id);

    List<Movie> getAll();

    boolean update(Movie movie);

    boolean delete(Long id);
}
