package com.wagner.auction.service;

import com.wagner.auction.dto.BidDTO;
import com.wagner.auction.dto.LotDTO;
import com.wagner.auction.model.Bid;
import com.wagner.auction.projection.FrequentView;
import com.wagner.auction.repository.BidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BidService {
    private final BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public BidDTO createBid(BidDTO bidDTO, LotDTO lotDTO) {
        Bid bid = bidDTO.toBid();
        bid.setBidDate(LocalDateTime.now());
        bid.setLot(lotDTO.toLot());
        return BidDTO.fromBid(bidRepository.save(bid));
    }

    public BidDTO findLastBid(Long id) {
        return BidDTO.fromBid(bidRepository.findLastBid(id));
    }

    public BidDTO findFirstBid(Long id) {
        return BidDTO.fromBid(bidRepository.findFirstBid(id));
    }

    public Integer countBidByLotId(Long id) {
        return bidRepository.countBidByLotId(id);
    }

    public FrequentView findFrequent(Long id){
        return bidRepository.findFrequent(id);
    }
}
