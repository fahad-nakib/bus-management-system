package com.busbooking.system.service;

import com.busbooking.system.dto.BusCreateRequest;
import com.busbooking.system.dto.BusUpdateRequest;
import com.busbooking.system.dto.BusResponse;

import java.util.List;

public interface BusService {
    BusResponse createBus(BusCreateRequest request);
    BusResponse updateBus(Integer busId, BusUpdateRequest request);
    BusResponse getBusById(Integer busId);
    List<BusResponse> getAllBuses();
    List<BusResponse> getActiveBuses();
    void deleteBus(Integer busId);
}