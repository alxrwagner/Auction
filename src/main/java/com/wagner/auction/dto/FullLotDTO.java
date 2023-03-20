package com.wagner.auction.dto;

import com.wagner.auction.enums.LotStatus;
import com.wagner.auction.model.Lot;
import lombok.Data;

@Data
public class FullLotDTO {
    private Long id;
    private LotStatus status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private String lastBid;

    public static FullLotDTO fromLot(Lot lot){
        FullLotDTO fullLotDTO = new FullLotDTO();
        fullLotDTO.setId(lot.getId());
        fullLotDTO.setStatus(lot.getStatus());
        fullLotDTO.setTitle(lot.getTitle());
        fullLotDTO.setDescription(lot.getDescription());
        fullLotDTO.setStartPrice(lot.getStartPrice());
        fullLotDTO.setBidPrice(lot.getBidPrice());

        return fullLotDTO;
    }


    public Lot toLot(){
        Lot lot = new Lot();
        lot.setId(this.getId());
        lot.setStatus(this.getStatus());
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());

        return lot;
    }
}
