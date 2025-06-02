package dev.samuel.currencyconverter.exception;

public class NotValidCurrencyCodeException extends Exception {
    public NotValidCurrencyCodeException(String message) {
        super(message);
    }
}
