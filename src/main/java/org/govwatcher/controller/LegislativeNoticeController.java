package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.LegislativeNoticeDto;
import org.govwatcher.service.LegislativeNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Slf4j
public class LegislativeNoticeController {

    private final LegislativeNoticeService noticeService;

//    @GetMapping
//    public ResponseEntity<List<LegislativeNoticeDto>> getAllNotices() {
//        return ResponseEntity.ok(noticeService.getAll());
//    }
//
//    @GetMapping("/{billId}")
//    public ResponseEntity<LegislativeNoticeDto> getNotice(@PathVariable String billId) {
//        return ResponseEntity.ok(noticeService.getByBillId(billId));
//    }
}
