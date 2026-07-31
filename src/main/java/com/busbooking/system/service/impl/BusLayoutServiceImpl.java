package com.busbooking.system.service.impl;

import com.busbooking.system.dto.BusLayoutRequestDTO;
import com.busbooking.system.dto.BusLayoutResponseDTO;
import com.busbooking.system.entity.BusLayout;
import com.busbooking.system.entity.Deck;
import com.busbooking.system.entity.Seat;
import com.busbooking.system.entity.enums.DeckTypeEnum;
import com.busbooking.system.entity.enums.SeatTypeEnum;
import com.busbooking.system.repository.BusLayoutRepository;
import com.busbooking.system.repository.DeckRepository;
import com.busbooking.system.repository.SeatRepository;
import com.busbooking.system.service.BusLayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusLayoutServiceImpl implements BusLayoutService {

    private final BusLayoutRepository layoutRepository;
    private final DeckRepository deckRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public BusLayoutResponseDTO createLayout(BusLayoutRequestDTO dto) {
        // ১. BusLayout অবজেক্ট তৈরি এবং ডাটাবেজে সেভ
        BusLayout layout = new BusLayout();
        BeanUtils.copyProperties(dto, layout);
        layout.setIsActive(true);
        layout.setCreatedAt(ZonedDateTime.now());

        BusLayout savedLayout = layoutRepository.save(layout);

        // ২. ডেক এবং সিট অটোমেটিক জেনারেট করা
        generateDecksAndSeats(savedLayout);

        return mapToResponse(savedLayout);
    }

    @Override
    @Transactional(readOnly = true)
    public BusLayoutResponseDTO getLayoutById(Integer id) {
        BusLayout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus Layout not found with ID: " + id));
        return mapToResponse(layout);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusLayoutResponseDTO> getAllActiveLayouts() {
        return layoutRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BusLayoutResponseDTO updateLayout(Integer id, BusLayoutRequestDTO dto) {
        BusLayout existingLayout = layoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus Layout not found with ID: " + id));

        // ১. পুরানো সিটগুলো ডিলিট করে দেওয়া (যেহেতু সিট লেআউট চেঞ্জ হতে পারে)
        seatRepository.deleteByBusLayoutLayoutId(id);
        // ২. পুরানো ডেকগুলোও ডিলিট করা
        deckRepository.deleteByBusLayoutLayoutId(id);

        // ৩. নতুন ডেটা আপডেট করা
        BeanUtils.copyProperties(dto, existingLayout, "layoutId", "createdAt");
        BusLayout updatedLayout = layoutRepository.save(existingLayout);

        // ৪. নতুন করে ডেক ও সিট জেনারেট করা
        generateDecksAndSeats(updatedLayout);

        return mapToResponse(updatedLayout);
    }

    @Override
    @Transactional
    public void deleteLayout(Integer id) {
        BusLayout existingLayout = layoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus Layout not found with ID: " + id));

        // সফট ডিলিট (ইন্ডাস্ট্রি স্ট্যান্ডার্ড প্র্যাকটিস)
        existingLayout.setIsActive(false);
        layoutRepository.save(existingLayout);
    }

    // --- অভ্যন্তরীণ সিট জেনারেশন লজিক (Private Helper Methods) ---

    private void generateDecksAndSeats(BusLayout layout) {
        List<Seat> seatsToSave = new ArrayList<>();

        // LOWER Deck জেনারেশন (যদি থাকে)
        if (Boolean.TRUE.equals(layout.getHasLowerDeck()) && layout.getRowsLower() != null) {
            Deck lowerDeck = new Deck();
            lowerDeck.setBusLayout(layout);
            lowerDeck.setDeckType(DeckTypeEnum.LOWER);
            lowerDeck.setDeckLabel("L");
            Deck savedLowerDeck = deckRepository.save(lowerDeck);

            generateSeatsForDeck(layout, savedLowerDeck, layout.getRowsLower(), "L", seatsToSave);
        }

        // UPPER Deck জেনারেশন (যদি থাকে)
        if (Boolean.TRUE.equals(layout.getHasUpperDeck()) && layout.getRowsUpper() != null) {
            Deck upperDeck = new Deck();
            upperDeck.setBusLayout(layout);
            upperDeck.setDeckType(DeckTypeEnum.UPPER);
            upperDeck.setDeckLabel("U");
            Deck savedUpperDeck = deckRepository.save(upperDeck);

            generateSeatsForDeck(layout, savedUpperDeck, layout.getRowsUpper(), "U", seatsToSave);
        }

        // মেমরি পারফরম্যান্স ভালো রাখার জন্য একসাথে সব সিট ব্যাচ সেভ (Bulk Insert) করা হচ্ছে
        seatRepository.saveAll(seatsToSave);
    }

    private void generateSeatsForDeck(BusLayout layout, Deck deck, short totalRows, String prefix, List<Seat> seatsToSave) {
        for (short row = 1; row <= totalRows; row++) {
            for (short col = 1; col <= layout.getColumnsPerRow(); col++) {

                // রো নম্বরকে ক্যারেক্টারে রূপান্তর (১ = A, ২ = B, ৩ = C...)
                char rowChar = (char) ('A' + row - 1);
                String seatLabel = prefix + "-" + rowChar + col; // e.g., L-A1, L-A2, U-A1

                // বাসের দুই পাশের সিটগুলোকে WINDOW এবং মাঝের সিটগুলোকে AISLE হিসেবে মার্ক করার লজিক
                SeatTypeEnum seatType = SeatTypeEnum.AISLE;
                if (col == 1 || col == layout.getColumnsPerRow()) {
                    seatType = SeatTypeEnum.WINDOW;
                }

                // আপনার Seat এনটিটির কনস্ট্রাক্টর অনুযায়ী অবজেক্ট ইনিশিয়ালাইজেশন
                Seat seat = new Seat();
                seat.setBusLayout(layout);
                seat.setDeck(deck);
                seat.setSeatLabel(seatLabel);
                seat.setRowNumber(row);
                seat.setColumnNumber(col);
                seat.setSeatType(seatType);
                seat.setIsLadiesSeat(false);
                seat.setIsActive(true);

                seatsToSave.add(seat);
            }
        }
    }

    private BusLayoutResponseDTO mapToResponse(BusLayout layout) {
        BusLayoutResponseDTO dto = new BusLayoutResponseDTO();
        BeanUtils.copyProperties(layout, dto);
        return dto;
    }
}