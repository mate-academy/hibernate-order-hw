package mate.academy;

import java.util.ArrayList;
import java.util.List;
import mate.academy.tests.CinemaHallServiceTest;
import mate.academy.tests.GlobalTest;
import mate.academy.tests.MovieServiceTest;
import mate.academy.tests.MovieSessionServiceTest;
import mate.academy.tests.TicketAppTest;
import mate.academy.tests.UserServiceTest;

public class Main {
    public static void main(String[] args) {
        testSolution();
    }

    private static void testSolution() {
        List<TicketAppTest> tests = new ArrayList<>();
        tests.add(new UserServiceTest());
        tests.add(new MovieServiceTest());
        tests.add(new CinemaHallServiceTest());
        tests.add(new MovieSessionServiceTest());
        tests.add(new GlobalTest());
        tests.forEach(t -> t.testClass(t.getClass().getSimpleName()));
    }
}
