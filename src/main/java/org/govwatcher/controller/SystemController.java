package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
@Slf4j
public class SystemController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check called");
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = Map.of(
                "politicians", 0,
                "bills", 0,
                "notices", 0,
                "timestamp", LocalDateTime.now()
        );
        return ResponseEntity.ok(status);
    }
}