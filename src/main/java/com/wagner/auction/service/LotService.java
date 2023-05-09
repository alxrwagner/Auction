package com.wagner.auction.service;

import com.wagner.auction.dto.*;
import com.wagner.auction.enums.LotStatus;
import com.wagner.auction.model.Lot;
import com.wagner.auction.projection.FrequentView;
import com.wagner.auction.repository.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class LotService {

    private final LotRepository lotRepository;
    private final BidService bidService;

    public LotService(LotRepository lotRepository, BidService bidService) {
        this.lotRepository = lotRepository;
        this.bidService = bidService;
    }

    public LotDTO createLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        lot.setStatus(LotStatus.CREATED);
        log.info("Lot has been created");
        return LotDTO.fromLot(lotRepository.save(lot));
    }

    public FullLotDTO findLotById(Long lotId) {
        Lot lot = lotRepository.findById(lotId).orElse(null);
        if (lot == null) {
            return null;
        }
        log.info("Find lot by id");
        return FullLotDTO.fromLot(lot);
    }

    public FullLotDTO getFullLot(Long id) {
        Lot lot = lotRepository.findById(id).orElse(null);

        if (lot != null) {
            FullLotDTO fullLotDTO = FullLotDTO.fromLot(lot);
            fullLotDTO.setCurrentPrice(getCurrentPrice(id));
            fullLotDTO.setLastBid(findLastBid(id));
            return fullLotDTO;
        }
        log.info("Full lot has got");
        return null;
    }

    private Integer getCurrentPrice(Long id) {
        Lot lot = findLotById(id).toLot();
        Integer countBids = bidService.countBidByLotId(id);

        log.info("Current cost has got");
        return countBids * lot.getBidPrice() + lot.getStartPrice();
    }

    private BidDTO findLastBid(Long id) {
        log.info("Found last bid was started");
        if (bidService.countBidByLotId(id) != 0) {
            return bidService.findLastBid(id);
        }
        return null;
    }

    public BidDTO findFirstBid(Long id) {
        BidDTO bidDTO = bidService.findFirstBid(id);
        if (bidDTO == null) {
            return null;
        }
        log.info("Find first bid");
        return bidDTO;
    }

    public FullLotDTO startBiding(FullLotDTO fullLotDTO) {
        Lot lot = fullLotDTO.toLot();
        lot.setStatus(LotStatus.STARTED);
        log.info("STARTED lot status is set");
        return FullLotDTO.fromLot(lotRepository.save(lot));
    }

    public LotDTO stopBiding(FullLotDTO fullLotDTO) {
        Lot lot = fullLotDTO.toLot();
        lot.setStatus(LotStatus.STOPPED);
        log.info("STOPPED lot status is set");
        return LotDTO.fromLot(lotRepository.save(lot));
    }

    public BidDTO placeBid(Long id, BidDTO bidDTO){
        LotDTO lotDTO = LotDTO.fromLot(lotRepository.findById(id).orElse(null));
        if (lotDTO == null){
            return null;
        }
        log.info("Place a bid");
        return bidService.createBid(bidDTO, lotDTO);
    }

    public FrequentView findFrequent(Long id){
        log.info("Find frequent was started");
        return bidService.findFrequent(id);
    }

    @Transactional
    public List<LotDTO> getAllLots(LotStatus status, Pageable pageable) {
        log.info("Was invoke method for find all lots by page");

        return lotRepository.findAllByStatus(status, pageable).stream().map(LotDTO::fromLot).collect(Collectors.toList());
    }

    public List<FullLotDTO> getExportList() {
        return lotRepository.findAll()
                .stream()
                .map(FullLotDTO::fromLot)
                .peek(fullLotDTO -> fullLotDTO.setCurrentPrice(getCurrentPrice(fullLotDTO.getId())))
                .peek(fullLotDTO -> fullLotDTO.setLastBid(findLastBid(fullLotDTO.getId())))
                .collect(Collectors.toList());
    }
}
