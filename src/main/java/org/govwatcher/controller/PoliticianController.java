package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.politician.PoliticianListResponse;
import org.govwatcher.dto.politician.detail.PoliticianDetailResponse;
import org.govwatcher.dto.politician.detail.network.PoliticianNetworkDto;
import org.govwatcher.service.PoliticianService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PoliticianController {

    private final PoliticianService politicianService;

    @GetMapping("/v1/politicians")
    public ResponseEntity<PoliticianListResponse> getPoliticians(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "party", defaultValue = "") String party,
            @RequestParam(name = "district", defaultValue = "") String district,
            @RequestParam(name = "sort", defaultValue = "bills") String sort
    ) {
        return ResponseEntity.ok(politicianService.getPoliticians(page, size, name, party, district, sort));
    }

    @GetMapping("/v1/politicians/detail/{id}")
    public ResponseEntity<PoliticianDetailResponse> getPoliticiansDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(politicianService.getPoliticiansDetail(id));
    }


    @GetMapping("/v1/politicians/network/{id}")
    public ResponseEntity<PoliticianNetworkDto> getPoliticianNetwork(@PathVariable("id") Long id) {
        return ResponseEntity.ok(politicianService.getPoliticianNetwork(id));
    }

}
