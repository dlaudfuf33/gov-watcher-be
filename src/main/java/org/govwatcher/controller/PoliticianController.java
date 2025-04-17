package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.PoliticianDto;
import org.govwatcher.service.PoliticianService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/politicians")
@RequiredArgsConstructor
@Slf4j
public class PoliticianController {

    private final PoliticianService politicianService;

//    @GetMapping
//    public ResponseEntity<List<PoliticianDto>> getAllPoliticians() {
//        return ResponseEntity.ok(politicianService.getAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PoliticianDto> getPolitician(@PathVariable Long id) {
//        return ResponseEntity.ok(politicianService.getById(id));
//    }
}
