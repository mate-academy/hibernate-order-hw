package mate.academy.service.impl;

import java.time.LocalDate;
import java.util.List;
import mate.academy.dao.MovieSessionDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.service.MovieSessionService;

@Service
public class MovieSessionServiceImpl implements MovieSessionService {
    @Inject
    private MovieSessionDao sessionDao;

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        return sessionDao.findAvailableSessions(movieId, date);
    }

    @Override
    public MovieSession get(Long id) {
        return sessionDao.get(id).get();
    }

    @Override
    public MovieSession add(MovieSession session) {
        return sessionDao.add(session);
    }
}
