package com.wagner.auction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.wagner.auction.jsonview.BidJsonView;
import com.wagner.auction.model.Bid;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidDTO {
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonView(BidJsonView.ViewBid.class)
//    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(BidJsonView.ViewCreateBid.class)
    private String bidderName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(BidJsonView.ViewBid.class)
    private LocalDateTime bidDate;
    @JsonIgnore
    private Long lotId;

    public static BidDTO fromBid(Bid bid){
        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());

        return bidDTO;
    }

    public Bid toBid(){
        Bid bid = new Bid();
        bid.setBidderName(this.bidderName);
        bid.setBidDate(this.getBidDate());

        return bid;
    }
}
