package org.govwatcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.govwatcher.dto.BillDto;
import org.govwatcher.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@Slf4j
public class BillController {

    private final BillService billService;

//    @GetMapping
//    public ResponseEntity<List<BillDto>> getAllBills() {
//        return ResponseEntity.ok(billService.getAll());
//    }
//
//    @GetMapping("/{billId}")
//    public ResponseEntity<BillDto> getBillDetail(@PathVariable String billId) {
//        return ResponseEntity.ok(billService.getById(billId));
//    }
}