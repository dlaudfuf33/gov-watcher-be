package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.enums.PrimarySortType;
import org.govwatcher.dto.enums.SecondarySortType;
import org.govwatcher.dto.legislativenotice.LegislativeNoticeDetailResponse;
import org.govwatcher.dto.legislativenotice.LegislativeNoticeResponse;
import org.govwatcher.service.LegislativeNoticeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/legislations")
public class LegislativeNoticeController {

    private final LegislativeNoticeService legislativeNoticeService;

    @GetMapping("/notices")
    public ResponseEntity<Page<LegislativeNoticeResponse>> getNotices(
            @RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam("primarySort") PrimarySortType primarySort,
            @RequestParam(value = "secondarySort", required = false, defaultValue = "NONE") SecondarySortType secondarySort) {
        Page<LegislativeNoticeResponse> responsePage = legislativeNoticeService.getNotices(page, size, primarySort, secondarySort);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LegislativeNoticeDetailResponse> getLegislationDetail(@PathVariable("id") Long id) {
        LegislativeNoticeDetailResponse response = legislativeNoticeService.getLegislationDetail(id);
        return ResponseEntity.ok(response);
    }
}
