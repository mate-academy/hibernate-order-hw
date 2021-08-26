package mate.academy.tests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.MovieSession;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;

public class MovieSessionServiceTest extends TicketAppTest {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static MovieSessionService movieSessionService = (MovieSessionService) injector
            .getInstance(MovieSessionService.class);
    private static MovieService movieService = (MovieService) injector
            .getInstance(MovieService.class);
    private static CinemaHallService cinemaHallService = (CinemaHallService) injector
            .getInstance(CinemaHallService.class);
    private static Long MOVIE_ID = movieService.getAll().get(0).getId();
    private static Long CINEMA_HALL_ID = cinemaHallService.getAll().get(0).getId();
    private MovieSession movieSession;
    private List<MovieSession> movieSessions = new ArrayList<>();
    private LocalDate today = LocalDate.now();

    @Override
    public void testAll() {
        add_Ok();
        add_NotOk();
        get_NotOk();
        findAvailableSessions_Ok();
    }

    private void add_Ok() {
        try {
            movieSession = new MovieSession();
            movieSession.setMovie(movieService.get(MOVIE_ID));
            movieSession.setCinemaHall(cinemaHallService.get(CINEMA_HALL_ID));
            movieSession.setShowTime(today.atTime(18, 30));
            movieSessionService.add(movieSession);
            movieSessions.add(movieSession);

            MovieSession lateNightSession = new MovieSession();
            lateNightSession.setMovie(movieService.get(MOVIE_ID));
            lateNightSession.setCinemaHall(cinemaHallService.get(CINEMA_HALL_ID));
            lateNightSession.setShowTime(today.plusDays(1).atTime(22, 40));
            movieSessionService.add(lateNightSession);
            movieSessions.add(lateNightSession);

            printTestPassedMessage("add_Ok");
        } catch (Exception e) {
            printTestFailedMessage("add_Ok", e.getMessage());
        }
    }

    private void add_NotOk() {
        try {
            movieSessionService.add(null);
            printTestFailedMessage("add_NotOk",
                    "Runtime exception should be thrown: passed NULL instead of movie session");
        } catch (Exception e) {
            printTestPassedMessage("add_NotOk");
        }
    }

    private void get_NotOk() {
        try {
            movieSessionService.get(-1L);
            printTestFailedMessage("get_NotOk", "Runtime exception should be thrown: "
                    + "shouldn't get movie session by invalid ID");
        } catch (Exception e) {
            printTestPassedMessage("get_NotOk");
        }
    }

    private void findAvailableSessions_Ok() {
        try {
            List<MovieSession> availableSessions = movieSessionService
                    .findAvailableSessions(MOVIE_ID, today);
            if (availableSessions != null && availableSessions.contains(movieSession)) {
                printTestPassedMessage("findAvailableSessions_Ok");
            } else {
                printTestFailedMessage("findAvailableSessions_Ok",
                        "Couldn't find available session");
            }
        } catch (Exception e) {
            printTestFailedMessage("findAvailableSessions_Ok", e.getMessage());
        }
    }
}
