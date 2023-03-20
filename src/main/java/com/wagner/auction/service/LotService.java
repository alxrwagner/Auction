package com.wagner.auction.service;

import com.wagner.auction.dto.*;
import com.wagner.auction.enums.LotStatus;
import com.wagner.auction.model.Bid;
import com.wagner.auction.model.Lot;
import com.wagner.auction.repository.BidRepository;
import com.wagner.auction.repository.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LotService {

    private final LotRepository lotRepository;
    private final BidService bidService;

    public LotService(LotRepository lotRepository, BidService bidService) {
        this.lotRepository = lotRepository;
        this.bidService = bidService;
    }

    @Transactional
    public LotDTO createLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        lot.setStatus(LotStatus.CREATED);
        log.info("Lot has been created");
        return LotDTO.fromLot(lotRepository.save(lot));
    }

    @Transactional
    public FullLotDTO findLotById(Long lotId) {
        Lot lot = lotRepository.findById(lotId).orElse(null);
        if (lot == null) {
            return null;
        }
        return FullLotDTO.fromLot(lot);
    }

    @Transactional
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

    private String findLastBid(Long id) {
        log.info("Found last bid was started");
        if (bidService.countBidByLotId(id) != 0) {
            return bidService.findLastBid(id).getBidderName();
        }
        return null;
    }

    public BidDTO findFirstBid(Long id) {
        BidDTO bidDTO = bidService.findFirstBid(id);
        if (bidDTO == null) {
            return null;
        }
        return bidDTO;
    }

    public FullLotDTO startBiding(FullLotDTO fullLotDTO) {
        Lot lot = fullLotDTO.toLot();
        lot.setStatus(LotStatus.STARTED);
        return FullLotDTO.fromLot(lotRepository.save(lot));
    }

    public LotDTO stopBiding(FullLotDTO fullLotDTO) {
        Lot lot = fullLotDTO.toLot();
        lot.setStatus(LotStatus.STOPPED);
        return LotDTO.fromLot(lotRepository.save(lot));
    }

    public BidDTO placeBid(Long id, BidDTO bidDTO){
        LotDTO lotDTO = LotDTO.fromLot(lotRepository.findById(id).orElse(null));
        if (lotDTO == null){
            return null;
        }
        return bidService.createBid(bidDTO, lotDTO);
    }

    public String findFrequent(Long id){
return null;
    }
}
