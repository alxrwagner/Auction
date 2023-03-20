package com.wagner.auction.repository;

import com.wagner.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query(value = "SELECT count(b) FROM bid b WHERE lot_id = ?1", nativeQuery = true)
    Integer countBidByLotId(Long id);

    @Query(value = "SELECT * FROM bid WHERE lot_id = ?1 ORDER BY bid_date DESC LIMIT 1", nativeQuery = true)
    Bid findLastBid(Long id);

    @Query(value = "SELECT * FROM bid WHERE lot_id = ?1 ORDER BY bid_date LIMIT 1", nativeQuery = true)
    Bid findFirstBid(Long id);

//    @Query(value = "")
//    Bid findFrequent(Long id);
}
