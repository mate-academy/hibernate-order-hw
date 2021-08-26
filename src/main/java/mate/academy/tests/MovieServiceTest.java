package mate.academy.tests;

import java.util.ArrayList;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.Movie;
import mate.academy.service.MovieService;

public class MovieServiceTest extends TicketAppTest {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static MovieService movieService = (MovieService) injector
            .getInstance(MovieService.class);
    private Movie movie;
    private List<Movie> movies = new ArrayList<>();

    @Override
    public void testAll() {
        add_Ok();
        add_NotOk();
        get_Ok();
        get_NotOk();
        getAll_Ok();
    }

    private void add_Ok() {
        try {
            movie = new Movie("For rooms");
            movie.setDescription("The story is set in the fictional Hotel Mon Signor in Los "
                    + "Angeles on New Year's Eve");
            movieService.add(movie);
            movies.add(movie);

            Movie pulpFiction = new Movie("Pulp fiction");
            pulpFiction.setDescription("It tells several stories of criminal Los Angeles");
            movieService.add(pulpFiction);
            movies.add(pulpFiction);

            printTestPassedMessage("add_Ok");
        } catch (Exception e) {
            printTestFailedMessage("add_Ok", e.getMessage());
        }
    }

    private void add_NotOk() {
        try {
            movieService.add(null);
            printTestFailedMessage("add_NotOk",
                    "Runtime exception should be thrown: passed NULL instead of movie");
        } catch (Exception e) {
            printTestPassedMessage("add_NotOk");
        }
    }

    private void get_Ok() {
        try {
            Movie forRoomsFromDB = movieService.get(movie.getId());
            if (forRoomsFromDB.equals(movie)) {
                printTestPassedMessage("get_Ok");
            } else {
                printTestFailedMessage("get_Ok", "Movie from DB not equals original movie: "
                        + "\n   FROM DB: " + forRoomsFromDB
                        + "\n   ORIGINAL: " + movie);
            }
        } catch (Exception e) {
            printTestFailedMessage("get_Ok", e.getMessage());
        }
    }

    private void get_NotOk() {
        try {
            movieService.get(-1L);
            printTestFailedMessage("get_NotOk",
                    "Runtime exception should be thrown: shouldn't get movie by invalid ID");
        } catch (Exception e) {
            printTestPassedMessage("get_NotOk");
        }
    }

    private void getAll_Ok() {
        try {
            List<Movie> allMoviesFromDB = movieService.getAll();
            if (allMoviesFromDB != null && allMoviesFromDB.size() > 0
                    && allMoviesFromDB.containsAll(movies)) {
                printTestPassedMessage("getAll_Ok");
            } else {
                printTestFailedMessage("getAll_Ok", "Couldn't get all movies from DB");
            }
        } catch (Exception e) {
            printTestFailedMessage("getAll_Ok", e.getMessage());
        }
    }
}
