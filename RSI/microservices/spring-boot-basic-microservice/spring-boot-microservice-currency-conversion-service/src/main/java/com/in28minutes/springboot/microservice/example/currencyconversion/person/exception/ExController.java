package com.in28minutes.springboot.microservice.example.currencyconversion.person.exception;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExController {
    @ResponseBody
    @ExceptionHandler(PersonExistsEx.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    ResponseEntity<?> PEEHandler(PersonExistsEx e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.NOT_FOUND)
                        .withTitle(HttpStatus.NOT_FOUND.name())
                        .withDetail(e.getMessage()));
    }
    @ResponseBody
    @ExceptionHandler(PersonNotFoundEx.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    ResponseEntity<?> PNFEHandler(PersonNotFoundEx e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.NOT_FOUND)
                        .withTitle(HttpStatus.NOT_FOUND.name())
                        .withDetail(e.getMessage()));
    }
    @ResponseBody
    @ExceptionHandler(ConflictEx.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    ResponseEntity<?> CEHandler(ConflictEx e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.NOT_FOUND)
                        .withTitle(HttpStatus.NOT_FOUND.name())
                        .withDetail(e.getMessage()));
    }
    @ResponseBody
    @ExceptionHandler(BadRequestEx.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ResponseEntity<?> BREHandler(BadRequestEx e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withStatus(HttpStatus.NOT_FOUND)
                        .withTitle(HttpStatus.NOT_FOUND.name())
                        .withDetail(e.getMessage()));
    }
}
