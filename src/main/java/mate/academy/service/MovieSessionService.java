package mate.academy.service;

import java.time.LocalDate;
import java.util.List;
import mate.academy.model.MovieSession;

public interface MovieSessionService {
    MovieSession add(MovieSession movieSession);

    MovieSession get(Long id);

    List<MovieSession> findAvailableSessions(Long movieId, LocalDate localDate);
}
