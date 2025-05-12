package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.dashboard.BillComiitteeStatsResponse;
import org.govwatcher.dto.dashboard.DemographicStatsResponse;
import org.govwatcher.dto.dashboard.ParliamentStatResponse;
import org.govwatcher.dto.dashboard.PartyDistributionResponse;
import org.govwatcher.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/parliament-stats")
    public ResponseEntity<ParliamentStatResponse> getParliamentStats() {
        return ResponseEntity.ok(dashboardService.getParliamentStats());
    }

    @GetMapping("/party-distribution")
    public ResponseEntity<PartyDistributionResponse> getPartyDistribution() {
        return ResponseEntity.ok(dashboardService.getPartyDistribution());
    }

    @GetMapping("/demographic-stats")
    public ResponseEntity<DemographicStatsResponse> getDemographicStats() {
        return ResponseEntity.ok(dashboardService.getDemographicStats());
    }

    @GetMapping("/committee-stats")
    public ResponseEntity<BillComiitteeStatsResponse> getCommitteeStats() {
        return ResponseEntity.ok(dashboardService.getCommitteeStats());
    }

    @GetMapping("/partybill-stats")
    public ResponseEntity<BillComiitteeStatsResponse> getCategoryStats() {
        return ResponseEntity.ok(dashboardService.gePartyBillStats());
    }

}