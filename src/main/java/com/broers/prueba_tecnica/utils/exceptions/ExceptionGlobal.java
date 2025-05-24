package com.broers.prueba_tecnica.utils.exceptions;

import com.broers.prueba_tecnica.utils.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ExceptionGlobal {

    // Manejo del error 404: Recurso no encontrado.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Manejo del error 403: Acceso denegado.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handle403(AccessDeniedException ex) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // Manejo del error 303: See Other (redirección) - ejemplo con excepción personalizada.
    @ExceptionHandler(SeeOtherException.class)
    public ResponseEntity<ApiError> handle303(SeeOtherException ex) {
        ApiError error = new ApiError(HttpStatus.SEE_OTHER.value(), "See Other", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SEE_OTHER);
    }

    // Manejo del error 503: Servicio no disponible.
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiError> handle503(ServiceUnavailableException ex) {
        ApiError error = new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Manejo del error 409: Conflicto.
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handle409(ConflictException ex) {
        ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Manejo del error 400: Solicitud incorrecta.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handle400(BadRequestException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Manejo del error 501: No implementado.
    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<ApiError> handle501(NotImplementedException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_IMPLEMENTED.value(), "Not Implemented", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_IMPLEMENTED);
    }

    // Manejo de cualquier otra excepción no controlada.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocurrió un error inesperado");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}