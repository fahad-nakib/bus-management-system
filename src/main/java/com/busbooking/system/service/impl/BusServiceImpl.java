package com.busbooking.system.service.impl;

import com.busbooking.system.dto.BusCreateRequest;
import com.busbooking.system.dto.BusUpdateRequest;
import com.busbooking.system.dto.BusResponse;
import com.busbooking.system.entity.Bus;
import com.busbooking.system.entity.BusLayout;
import com.busbooking.system.repository.BusLayoutRepository;
import com.busbooking.system.repository.BusRepository;
import com.busbooking.system.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final BusLayoutRepository busLayoutRepository;

    @Override
    @Transactional
    public BusResponse createBus(BusCreateRequest request) {
        if (busRepository.existsByBusNumber(request.busNumber())) {
            throw new IllegalArgumentException("Bus with number '" + request.busNumber() + "' already exists.");
        }
        if (request.registrationNumber() != null && busRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new IllegalArgumentException("Bus with registration number '" + request.registrationNumber() + "' already exists.");
        }

        BusLayout layout = busLayoutRepository.findById(request.layoutId())
                .orElseThrow(() -> new IllegalArgumentException("BusLayout not found with ID: " + request.layoutId()));

        Bus bus = new Bus();
        bus.setBusNumber(request.busNumber());
        bus.setRegistrationNumber(request.registrationNumber());
        bus.setBusLayout(layout);
        bus.setAcType(request.acType());
        bus.setClassType(request.classType());
        bus.setManufacturer(request.manufacturer());
        bus.setModelYear(request.modelYear());
        // Automatically set total_seats from the referenced layout
        bus.setTotalSeats(layout.getTotalSeats());
        bus.setIsActive(request.isActive() != null ? request.isActive() : true);
        bus.setCreatedAt(ZonedDateTime.now());

        Bus savedBus = busRepository.save(bus);
        return mapToResponse(savedBus);
    }

    @Override
    @Transactional
    public BusResponse updateBus(Integer busId, BusUpdateRequest request) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));

        if (busRepository.existsByBusNumberAndBusIdNot(request.busNumber(), busId)) {
            throw new IllegalArgumentException("Another bus already uses bus number '" + request.busNumber() + "'.");
        }
        if (request.registrationNumber() != null && busRepository.existsByRegistrationNumberAndBusIdNot(request.registrationNumber(), busId)) {
            throw new IllegalArgumentException("Another bus already uses registration number '" + request.registrationNumber() + "'.");
        }

        if (!bus.getBusLayout().getLayoutId().equals(request.layoutId())) {
            BusLayout newLayout = busLayoutRepository.findById(request.layoutId())
                    .orElseThrow(() -> new IllegalArgumentException("BusLayout not found with ID: " + request.layoutId()));
            bus.setBusLayout(newLayout);
            bus.setTotalSeats(newLayout.getTotalSeats());
        }

        bus.setBusNumber(request.busNumber());
        bus.setRegistrationNumber(request.registrationNumber());
        bus.setAcType(request.acType());
        bus.setClassType(request.classType());
        bus.setManufacturer(request.manufacturer());
        bus.setModelYear(request.modelYear());
        if (request.isActive() != null) {
            bus.setIsActive(request.isActive());
        }

        Bus updatedBus = busRepository.save(bus);
        return mapToResponse(updatedBus);
    }

    @Override
    @Transactional(readOnly = true)
    public BusResponse getBusById(Integer busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        return mapToResponse(bus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusResponse> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusResponse> getActiveBuses() {
        return busRepository.findByIsActive(true).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBus(Integer busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        bus.setIsActive(false);
        busRepository.save(bus);
    }

    private BusResponse mapToResponse(Bus bus) {
        return new BusResponse(
                bus.getBusId(),
                bus.getBusNumber(),
                bus.getRegistrationNumber(),
                bus.getBusLayout().getLayoutId(),
                bus.getBusLayout().getLayoutName(),
                bus.getAcType(),
                bus.getClassType(),
                bus.getManufacturer(),
                bus.getModelYear(),
                bus.getTotalSeats(),
                bus.getIsActive(),
                bus.getCreatedAt()
        );
    }
}