package com.algoritmando.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        int status,
        String mensagem,
        Map<String, String> campos,
        LocalDateTime timestamp
) {
}