package com.wagner.auction.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.wagner.auction.dto.*;
import com.wagner.auction.enums.LotStatus;
import com.wagner.auction.jsonview.BidJsonView;
import com.wagner.auction.service.LotService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping
public class LotController {

    public final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/lot/{id}/first")
    public ResponseEntity<?> getFirstBidder(@PathVariable Long id) {
        FullLotDTO fullLotDTO = lotService.findLotById(id);
        if (fullLotDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }else if (lotService.findFirstBid(id) == null){
            return ResponseEntity.ok(new BidDTO());
        }
        return ResponseEntity.ok(lotService.findFirstBid(id));
    }

    @GetMapping("/lot/{id}/frequent")
    public ResponseEntity<BidDTO> getMostFrequentBidder(@PathVariable Long id) {
        return ResponseEntity.ok(lotService.findFrequent(id));
    }

    @GetMapping("/lot/{id}")
    public ResponseEntity<?> getFullLot(@PathVariable Long id){
        FullLotDTO fullLotDTO = lotService.getFullLot(id);
        if (fullLotDTO != null){
            return ResponseEntity.ok(fullLotDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
    }

    @PostMapping("/lot/{id}/start")
    public ResponseEntity<String> startLotById(@PathVariable Long id){
        FullLotDTO fullLotDTO = lotService.findLotById(id);
        if (fullLotDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        } else if (fullLotDTO.getStatus() == LotStatus.STOPPED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Лот в неверном статусе");
        } else if (fullLotDTO.getStatus() == LotStatus.STARTED) {
            return ResponseEntity.ok("");
        }
        lotService.startBiding(fullLotDTO);
        return ResponseEntity.ok("Лот переведен в статус начато");
    }

    @PostMapping("/lot/{id}/bid")
    @JsonView(BidJsonView.ViewCreateBid.class)
    public ResponseEntity<String> createBid(@PathVariable Long id, @RequestBody BidDTO bidDTO){
        FullLotDTO fullLotDTO = lotService.findLotById(id);
        if(fullLotDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        } else if (fullLotDTO.getStatus() != LotStatus.STARTED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Лот в неверном статусе");
        }else {
            lotService.placeBid(id, bidDTO);
            return ResponseEntity.ok("Ставка создана");
        }
    }

    @PostMapping("/lot/{id}/stop")
    public ResponseEntity<String> stopLotById(@PathVariable Long id){
        FullLotDTO fullLotDTO = lotService.findLotById(id);

        if (fullLotDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        } else if (fullLotDTO.getStatus() == LotStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Торги по этому лоту еще не начаты");
        } else if (fullLotDTO.getStatus() == LotStatus.STOPPED) {
            return ResponseEntity.ok("");
        }
        lotService.stopBiding(fullLotDTO);
        return ResponseEntity.ok("Лот перемещен в статус остановлен");
    }

    @PostMapping("/lot")
    public LotDTO createLot(@RequestBody CreateLot createLot){
        return lotService.createLot(createLot);
    }

    @GetMapping("/lot")
    public ResponseEntity<List<LotDTO>> getAll(@RequestParam LotStatus status, Pageable pageable){
        return ResponseEntity.ok(lotService.getAllLots(status, pageable));
    }

    @GetMapping("/lot/export")
    public ResponseEntity<String> getCSVFile(HttpServletResponse response) throws IOException {
        List<FullLotDTO> lotsList = lotService.getExportList();
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);

        lotsList.forEach(fullLotDTO ->
        {
            try {
                printer.printRecord(fullLotDTO.getId(),
                        fullLotDTO.getTitle(),
                        fullLotDTO.getStatus(),
                        fullLotDTO.getLastBid() != null ? fullLotDTO.getLastBid().getBidderName() : "empty",
                        fullLotDTO.getCurrentPrice());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        printer.flush();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"lots.csv\"");
        PrintWriter writer = response.getWriter();
        writer.write(writer.toString());
        writer.flush();
        writer.close();
        return ResponseEntity.ok().build();
    }
}
