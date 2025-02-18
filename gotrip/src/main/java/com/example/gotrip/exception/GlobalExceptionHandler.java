package com.example.gotrip.exception;

import com.example.gotrip.util.SeatType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de validación de los argumentos de los métodos
     * (MethodArgumentNotValidException) lanzadas cuando los datos de entrada no
     * cumplen las restricciones de validación.
     *
     * @param ex La excepción de validación que contiene los errores de campo.
     * @return Una respuesta HTTP con el estado BAD_REQUEST (400) y un mapa con los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Optional.ofNullable(error.getDefaultMessage())
                                .orElse("Error en la validación"),
                        (msg1, msg2) -> msg1
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Maneja las excepciones de formato inválido en la solicitud HTTP,
     * específicamente cuando la fecha no tiene el formato correcto.
     *
     * @param ex La excepción de formato inválido.
     * @return Una respuesta HTTP con el estado BAD_REQUEST (400) y un mensaje de error específico sobre el formato de fecha.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidFormat(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException cause && cause.getTargetType() == LocalDate.class) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato de fecha incorrecto. Usa 'yyyy-MM-dd'.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la solicitud: formato inválido.");
    }

    /**
     * Maneja las excepciones de fecha inválida.
     *
     * @param ex La excepción de fecha inválida.
     * @return Una respuesta HTTP con el estado BAD_REQUEST (400) y el mensaje de la excepción.
     */
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones relacionadas con vuelos (FlightException).
     *
     * @param ex La excepción de vuelo.
     * @return Una respuesta HTTP con el estado BAD_REQUEST (400) y el mensaje de la excepción.
     */
    @ExceptionHandler(FlightException.class)
    public ResponseEntity<String> handleFlightException(FlightException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones relacionadas con hoteles (HotelException).
     *
     * @param ex La excepción de hotel.
     * @return Una respuesta HTTP con el estado BAD_REQUEST (400) y el mensaje de la excepción.
     */
    @ExceptionHandler(HotelException.class)
    public ResponseEntity<String> handleHotelException(HotelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones cuando no se encuentra el recurso solicitado.
     *
     * @param ex La excepción de recurso no encontrado.
     * @return Una respuesta HTTP con el estado NOT_FOUND (404) y un mensaje con detalles sobre el recurso no encontrado.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("El recurso solicitado no se ha encontrado: " + ex.getMessage());
    }

    /**
     * Maneja las excepciones de recurso no encontrado (ResourceNotFoundException).
     *
     * @param ex La excepción de recurso no encontrado.
     * @return Una respuesta HTTP con el estado NOT_FOUND (404) y el mensaje de la excepción.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> handleNoContentException(NoContentException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }

}
