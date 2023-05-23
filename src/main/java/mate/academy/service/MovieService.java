package mate.academy.service;

import java.util.List;
import mate.academy.model.Movie;

public interface MovieService extends AbstractService<Movie> {
    Movie get(Long id);

    List<Movie> getAll();
}
