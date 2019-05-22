package com.zenika.academy.woodblocktoys.Barrel;


import com.zenika.academy.woodblocktoys.Block.Block;
import com.zenika.academy.woodblocktoys.Block.BlockFactory;
import com.zenika.academy.woodblocktoys.Block.BlockRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(path = "/barrels")
public class BarrelController {


    /************************VARIABLES & CONSTRUCTOR************************/
    private final BarrelRepository barrelRepository;
    private final BlockFactory blockFactory;

    public BarrelController(BarrelRepository barrelRepository, BlockFactory blockFactory, BlockRepository blockRepository) {
        this.barrelRepository = barrelRepository;
        this.blockFactory = blockFactory;
    }

    /************************HELPER METHODS************************/
    public double roundToTwoDecimals(double d) {
        return Precision.round(d, 3);
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/{maxPrice}/{paintChoice}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Barrel> createBarrel(@PathVariable double maxPrice, @PathVariable String paintChoice) {
        log.info("Building a barrel with a max price of {} € with paint {}", maxPrice, paintChoice);
        double currentBarrelPrice = 0;
        int currentBarrelQty = 0;
        List<Block> currentBlockList = new ArrayList<>();
        while (currentBarrelPrice < maxPrice) {
            Block currentBlock = blockFactory.makeBlock(paintChoice);
            currentBarrelQty += 1;
            currentBlockList.add(currentBlock);
            if (currentBarrelPrice + currentBlock.getPrice() > maxPrice) {
                break;
            }
            currentBarrelPrice += currentBlock.getPrice();
        }
        Barrel newBarrel = Barrel.builder()
                .blockQuantity(currentBarrelQty)
                .price(roundToTwoDecimals(currentBarrelPrice))
                .blockList(currentBlockList)
                .build();

        return ResponseEntity.ok(barrelRepository.save(newBarrel));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBarrel(@PathVariable("id") long id) {
        log.info("Trying to delete barrel with id: {}", id);
        Optional<Barrel> optionalBarrel = barrelRepository.findById(id);
        if (optionalBarrel.isEmpty()) {
            log.info("Unable to delete barrel data with id: {}, barrel not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Barrel not found");
        }
        barrelRepository.deleteById(id);
        return new ResponseEntity<>("Barrel has been deleted!", HttpStatus.OK);
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Barrel>> getAllBarrels() {
        log.info("Trying to fetch barrel data");
        List<Barrel> barrelList = new ArrayList<>();
        barrelRepository.findAll().forEach(barrelList::add);
        if (barrelList.isEmpty()) {
            log.info("Unable to fetch any barrel data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No barrels in database");
        }
        return ResponseEntity.ok().body(barrelList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Barrel> findBarrelById(@PathVariable long id) {
        log.info("Trying to fetch barrel data with id: {}", id);
        Optional<Barrel> optionalBarrel = barrelRepository.findById(id);
        if (optionalBarrel.isEmpty()) {
            log.info("Unable to fetch barrel data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Barrel not found");
        }
        return ResponseEntity.ok().body(optionalBarrel.get());
    }

}
