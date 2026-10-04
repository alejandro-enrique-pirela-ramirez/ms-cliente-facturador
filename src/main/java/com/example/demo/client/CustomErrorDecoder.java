package com.example.demo.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());

        if (status == null) {
            return new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Error genérico: " + response.reason());
        }

        switch (status) {
            case NOT_FOUND:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "el recurso no fue encontrado");
            case BAD_REQUEST:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "solicitud inválida");
            case UNAUTHORIZED:
            case FORBIDDEN:
                return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "token de la API inválido o sin permisos");
            case TOO_MANY_REQUESTS:
                return new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "se superó el límite de consultas de la API");
            case INTERNAL_SERVER_ERROR:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error interno del servidor externo");
            default:
                return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error genérico: " + response.reason());
        }
    }
}