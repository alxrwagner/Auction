package com.wagner.auction.projection;

import java.time.LocalDateTime;

public interface FrequentView {
    String getBidderName();
    LocalDateTime getBidDate();
}
