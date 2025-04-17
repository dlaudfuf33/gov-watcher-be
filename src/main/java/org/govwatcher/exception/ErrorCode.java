package org.govwatcher.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    INVALID_INPUT_VALUE("C001", "Invalid input value", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("C002", "Entity not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTITY("C003", "Entity already exists", HttpStatus.CONFLICT),

    // 403
    FORBIDDEN_ACCESS("C004", "Access is forbidden", HttpStatus.FORBIDDEN),

    // 500
    INTERNAL_SERVER_ERROR("S001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}