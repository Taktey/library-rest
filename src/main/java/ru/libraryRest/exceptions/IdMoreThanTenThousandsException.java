package ru.libraryRest.exceptions;

public class IdMoreThanTenThousandsException extends RuntimeException {
    public IdMoreThanTenThousandsException(String message) {
        super(message);
    }
}
