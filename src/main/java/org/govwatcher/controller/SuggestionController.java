package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.enums.SuggestionStatus;
import org.govwatcher.dto.suggestion.SuggestionRequest;
import org.govwatcher.dto.suggestion.SuggestionResponse;
import org.govwatcher.dto.suggestion.SuggestionVoteRequest;
import org.govwatcher.service.SuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SuggestionController {
    private final SuggestionService suggestionService;


    @PostMapping("/v1/suggestions")
    public ResponseEntity<Void> submit(@RequestBody SuggestionRequest request) {
        suggestionService.submit(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/suggestions")
    public List<SuggestionResponse> getSuggestions(
            @RequestParam(name = "status", required = false) String status) {
        SuggestionStatus enumStatus = null;
        if (status != null) {
            enumStatus = SuggestionStatus.from(status);
        }
        return suggestionService.findByStatus(enumStatus);
    }

    @PatchMapping("/v1/suggestions/{id}/vote")
    public ResponseEntity<Void> voteSuggestion(
            @PathVariable("id") Long id,
            @RequestBody SuggestionVoteRequest request) {
        suggestionService.vote(id, request.getVisitorId(), request.isUp());
        return ResponseEntity.ok().build();
    }

}
