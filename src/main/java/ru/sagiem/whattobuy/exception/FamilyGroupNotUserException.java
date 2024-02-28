package ru.sagiem.whattobuy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FamilyGroupNotUserException extends RuntimeException{
}
