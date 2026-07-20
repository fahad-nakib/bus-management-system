package com.busbooking.system.service;

import com.busbooking.system.dto.BusLayoutRequestDTO;
import com.busbooking.system.dto.BusLayoutResponseDTO;

import java.util.List;

public interface BusLayoutService {
    BusLayoutResponseDTO createLayout(BusLayoutRequestDTO requestDTO);
    BusLayoutResponseDTO getLayoutById(Integer id);
    List<BusLayoutResponseDTO> getAllActiveLayouts();
    BusLayoutResponseDTO updateLayout(Integer id, BusLayoutRequestDTO requestDTO);
    void deleteLayout(Integer id);
}