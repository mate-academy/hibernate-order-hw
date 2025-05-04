package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.MovieDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.Movie;
import mate.academy.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
    @Inject
    private MovieDao movieDao;

    @Override
    public Movie add(Movie movie) {
        return movieDao.add(movie);
    }

    @Override
    public Movie get(Long id) {
        return movieDao.get(id).orElseThrow(
                () -> new RuntimeException("Movie with id " + id + " not found")
        );
    }

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }

    @Override
    public boolean update(Movie movie) {
        Movie currentMovie = get(movie.getId());
        movieDao.update(movie);
        return currentMovie.equals(get(movie.getId()));
    }

    @Override
    public boolean delete(Long id) {
        return movieDao.delete(id);
    }
}
