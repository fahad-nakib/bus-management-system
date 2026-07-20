package com.busbooking.system.controller;

import com.busbooking.system.dto.BusLayoutRequestDTO;
import com.busbooking.system.dto.BusLayoutResponseDTO;
import com.busbooking.system.service.BusLayoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bus-layouts")
@RequiredArgsConstructor
public class BusLayoutController {

    private final BusLayoutService layoutService;

    @PostMapping
    public ResponseEntity<BusLayoutResponseDTO> createLayout(@Valid @RequestBody BusLayoutRequestDTO requestDTO) {
        BusLayoutResponseDTO response = layoutService.createLayout(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusLayoutResponseDTO> getLayoutById(@PathVariable Integer id) {
        BusLayoutResponseDTO response = layoutService.getLayoutById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BusLayoutResponseDTO>> getAllActiveLayouts() {
        List<BusLayoutResponseDTO> response = layoutService.getAllActiveLayouts();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusLayoutResponseDTO> updateLayout(
            @PathVariable Integer id,
            @Valid @RequestBody BusLayoutRequestDTO requestDTO) {
        BusLayoutResponseDTO response = layoutService.updateLayout(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLayout(@PathVariable Integer id) {
        layoutService.deleteLayout(id);
        return ResponseEntity.noContent().build();
    }
}