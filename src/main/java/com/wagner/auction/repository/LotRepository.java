package com.wagner.auction.repository;

import com.wagner.auction.enums.LotStatus;
import com.wagner.auction.model.Lot;
import jdk.jshell.Snippet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findAllByStatus(LotStatus status, Pageable pageable);
}
