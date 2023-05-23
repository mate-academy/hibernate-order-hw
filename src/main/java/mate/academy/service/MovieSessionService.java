package mate.academy.service;

import java.time.LocalDate;
import java.util.List;
import mate.academy.model.MovieSession;

public interface MovieSessionService extends AbstractService<MovieSession> {
    List<MovieSession> findAvailableSessions(Long movieId, LocalDate date);

    MovieSession get(Long id);
}
