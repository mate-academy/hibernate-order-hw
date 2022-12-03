package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Movie;

public interface MovieDao extends GenericDao<Movie> {
    Optional<Movie> get(Long id);

    List<Movie> getAll();
}
