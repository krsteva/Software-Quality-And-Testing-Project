package mk.finki.ukim.mk.lab.assertions;

public class AssertionsException extends RuntimeException{
    public AssertionsException(String message, Object expected, Object actual) {
        super(String.format("%s:\texpected: <%s>\tactual:\t<%s>", message, expected.toString(), actual.toString()));
    }
}
