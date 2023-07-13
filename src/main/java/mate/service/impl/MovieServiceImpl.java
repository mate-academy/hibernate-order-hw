package mate.service.impl;

import java.util.List;
import mate.dao.MovieDao;
import mate.lib.Inject;
import mate.lib.Service;
import mate.model.Movie;
import mate.service.MovieService;

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
        return movieDao.get(id).get();
    }

    @Override
    public List<Movie> getAll() {
        return movieDao.getAll();
    }
}
