package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.BillRankingDto;
import org.govwatcher.dto.PoliticianRankingDto;
import org.govwatcher.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

//    @GetMapping("/top-bills")
//    public ResponseEntity<List<BillRankingDto>> getTopBills() {
//        return ResponseEntity.ok(dashboardService.getTopRankedBills());
//    }
//
//    @GetMapping("/politician-ranking")
//    public ResponseEntity<List<PoliticianRankingDto>> getPoliticianRanking() {
//        return ResponseEntity.ok(dashboardService.getTopPoliticians());
//    }
}