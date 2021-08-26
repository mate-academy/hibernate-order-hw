package mate.academy.tests;

import java.util.Optional;
import java.util.Random;
import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.service.UserService;

public class UserServiceTest extends TicketAppTest {
    private static Injector injector = Injector.getInstance("mate.academy");
    private static UserService userService = (UserService) injector.getInstance(UserService.class);
    private static Random random = new Random(5000);
    private static final String USER_EMAIL = "User" + random.nextInt() + "@gmail.com";

    @Override
    public void testAll() {
        add_Ok();
        add_NotOk();
        findByEmail_Ok();
        findByEmail_NotOk();
    }

    private void add_Ok() {
        try {
            User user = new User();
            user.setEmail(USER_EMAIL);
            user.setPassword("Q9w8E7r6T5y4");
            userService.add(user);
            printTestPassedMessage("add_Ok");
        } catch (Exception e) {
            printTestFailedMessage("add_Ok", e.getMessage());
        }
    }

    private void add_NotOk() {
        try {
            userService.add(null);
            printTestFailedMessage("add_NotOk",
                    "Runtime exception should be thrown: passed NULL instead of user");
        } catch (Exception e) {
            printTestPassedMessage("add_NotOk");
        }
    }

    private void findByEmail_Ok() {
        try {
            userService.findByEmail(USER_EMAIL).get();
            printTestPassedMessage("findByEmail_Ok");
        } catch (Exception e) {
            printTestFailedMessage("findByEmail_Ok", e.getMessage());
        }
    }

    private void findByEmail_NotOk() {
        Optional<User> unknown = userService.findByEmail("UnknownEMail@gmail.com");
        if (unknown.isEmpty()) {
            printTestPassedMessage("findByEmail_NotOk");
        } else {
            printTestFailedMessage("findByEmail_NotOk",
                    "Optional must be empty when e-mail is not in DB");
        }
    }
}
