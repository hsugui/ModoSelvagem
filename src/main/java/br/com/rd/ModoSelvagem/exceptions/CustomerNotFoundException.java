package br.com.rd.ModoSelvagem.exceptions;

public class CustomerNotFoundException extends Throwable {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
