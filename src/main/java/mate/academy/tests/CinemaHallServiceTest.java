package mate.academy.tests;

import java.util.ArrayList;
import java.util.List;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.service.CinemaHallService;

public class CinemaHallServiceTest extends TicketAppTest {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static CinemaHallService cinemaHallService = (CinemaHallService) injector
            .getInstance(CinemaHallService.class);
    private CinemaHall cinemaHall;
    private List<CinemaHall> cinemaHalls = new ArrayList<>();

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
            cinemaHall = new CinemaHall();
            cinemaHall.setCapacity(180);
            cinemaHall.setDescription("Main hall for 2D and 3D movies");
            cinemaHallService.add(cinemaHall);
            cinemaHalls.add(cinemaHall);

            CinemaHall vipHall = new CinemaHall();
            vipHall.setCapacity(15);
            vipHall.setDescription("VIP hall for VIP persons");
            cinemaHallService.add(vipHall);
            cinemaHalls.add(vipHall);

            printTestPassedMessage("add_Ok");
        } catch (Exception e) {
            printTestFailedMessage("add_Ok", e.getMessage());
        }
    }

    private void add_NotOk() {
        try {
            cinemaHallService.add(null);
            printTestFailedMessage("add_NotOk",
                    "Runtime exception should be thrown: passed NULL instead of cinema hall");
        } catch (Exception e) {
            printTestPassedMessage("add_NotOk");
        }
    }

    private void get_Ok() {
        try {
            CinemaHall cinemaHallFromDB = cinemaHallService.get(cinemaHall.getId());
            if (cinemaHallFromDB.equals(cinemaHall)) {
                printTestPassedMessage("get_Ok");
            } else {
                printTestFailedMessage("get_Ok", "Cinema hall from DB not equals original hall: "
                        + "\n   FROM DB: " + cinemaHallFromDB
                        + "\n   ORIGINAL: " + cinemaHall);
            }
        } catch (Exception e) {
            printTestFailedMessage("get_Ok", e.getMessage());
        }
    }

    private void get_NotOk() {
        try {
            cinemaHallService.get(-1L);
            printTestFailedMessage("get_NotOk",
                    "Runtime exception should be thrown: shouldn't get cinema hall by invalid ID");
        } catch (Exception e) {
            printTestPassedMessage("get_NotOk");
        }
    }

    private void getAll_Ok() {
        try {
            List<CinemaHall> allCinemaHallsFromDB = cinemaHallService.getAll();
            if (allCinemaHallsFromDB != null && allCinemaHallsFromDB.size() > 0
                    && allCinemaHallsFromDB.containsAll(cinemaHalls)) {
                printTestPassedMessage("getAll_Ok");
            } else {
                printTestFailedMessage("getAll_Ok", "Couldn't get all cinema halls from DB");
            }
        } catch (Exception e) {
            printTestFailedMessage("getAll_Ok", e.getMessage());
        }
    }
}
