package com.is.IS_4k1_TFI_G2.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class exceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ElPacienteYaExisteExcepcion.class)
    public ResponseEntity<MensajeError> ElPacienteYaExisteExcepcion(ElPacienteYaExisteExcepcion e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MensajeError(
                        status,
                        e.getMessage()
                ),
                status
        );
    }
    @ExceptionHandler(ElPacienteNoExisteExcepcion.class)
    public ResponseEntity<MensajeError> ElPacienteNoExisteExcepcion(ElPacienteNoExisteExcepcion e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MensajeError(
                        status,
                        e.getMessage()
                ),
                status
        );
    }
    @ExceptionHandler(ElPacienteEstaSuspendidoExcepcion.class)
    public ResponseEntity<MensajeError> ElPacienteEstaSuspendidoExcepcion(ElPacienteEstaSuspendidoExcepcion e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MensajeError(
                        status,
                        e.getMessage()
                ),
                status
        );
    }
    @ExceptionHandler(ObraSocialIncorrectaExcepcion.class)
    public ResponseEntity<MensajeError> ObraSocialIncorrectaExcepcion(ObraSocialIncorrectaExcepcion e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MensajeError(
                        status,
                        e.getMessage()
                ),
                status
        );
    }
    @ExceptionHandler(NroAfiliadoIncorrectoExcepcion.class)
    public ResponseEntity<MensajeError> NroAfiliadoIncorrectoExcepcion(NroAfiliadoIncorrectoExcepcion e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MensajeError(
                        status,
                        e.getMessage()
                ),
                status
        );
    }
}