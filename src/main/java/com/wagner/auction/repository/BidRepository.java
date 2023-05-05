package com.wagner.auction.repository;

import com.wagner.auction.model.Bid;
import com.wagner.auction.projection.FrequentView;
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
      
    @Query(value = "select bidder_name as bidderName, bid_date as bidDate from (select * from bid where bidder_name = (\n" +
            "select bidder_name from bid where lot_id = ?1 group by bidder_name order by count(*) desc limit 1)) as \"b*\" order by bid_date desc limit 1", nativeQuery = true)
    FrequentView findFrequent(Long id);
}
