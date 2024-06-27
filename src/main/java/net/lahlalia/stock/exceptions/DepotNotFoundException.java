package net.lahlalia.stock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DepotNotFoundException extends RuntimeException{
    public DepotNotFoundException(String message) {
        super(message);
    }
}