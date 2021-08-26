package mate.academy.tests;

import java.time.LocalTime;

public abstract class TicketAppTest {

    public void testClass(String serviceName) {
        System.out.println("\n[" + serviceName + ": STARTS AT " + LocalTime.now());
        try {
            testAll();
            System.out.println(serviceName + ": ENDS AT " + LocalTime.now() + "]");
        } catch (Exception e) {
            System.out.println(serviceName + ": ENDS WITH EXCEPTIONS AT "
                    + LocalTime.now() + "]\n");
        }
    }

    protected abstract void testAll();

    protected void printTestPassedMessage(String testName) {
        System.out.println("    + Test " + testName + " had been passed successfully");
    }

    protected void printTestFailedMessage(String testName, String exception) {
        System.out.println("    - Test " + testName + " had been FAILED: " + exception);
    }
}
