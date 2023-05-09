package com.wagner.auction.dto;

import com.wagner.auction.model.Lot;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateLot {
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;

    public Lot toLot(){
        Lot lot = new Lot();
        lot.setTitle(getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.startPrice);
        lot.setBidPrice(this.getBidPrice());

        return lot;
    }
}
