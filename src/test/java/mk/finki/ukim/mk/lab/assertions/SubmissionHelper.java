package mk.finki.ukim.mk.lab.assertions;

import java.util.ArrayList;

public class SubmissionHelper {
    public static ArrayList<String> log = new ArrayList<>();
    public static ArrayList<Exception> errors = new ArrayList<>();
    public static boolean hasError = false;
    public static String test;


    public static void successAssert(String message, Object expected, Object actual) {
        log.add(String.format("O;%s;%s", test, message));
    }

    public static void failedAssert(String message, Object expected, Object actual) {
        log.add(String.format("X;%s;%s:\texpected: <%s>\tactual:\t<%s>", test, message, expected.toString(), actual.toString()));
        errors.add(new AssertionsException(message, expected, actual));
        hasError = true;
    }
}
