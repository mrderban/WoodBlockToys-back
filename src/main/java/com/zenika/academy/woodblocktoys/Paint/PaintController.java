package com.zenika.academy.woodblocktoys.Paint;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Slf4j
@RequestMapping(path = "/paints")
public class PaintController {


    /************************VARIABLES & CONSTRUCTOR************************/
    private final PaintRepository paintRepository;

    public PaintController(PaintRepository paintRepository) {
        this.paintRepository = paintRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Paint> createPaint(@Valid @RequestBody Paint paint) {
        log.info("Paint registration submitted " + paint.toString());
        try {
            Paint resultPaint = paintRepository.save(paint);
            return ResponseEntity.ok(resultPaint);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deletePaint(@PathVariable("id") long id) {
        log.info("Trying to delete paint with id: {}", id);
        Optional<Paint> optionalPaint = paintRepository.findById(id);
        if (optionalPaint.isEmpty()) {
            log.info("Unable to delete paint data with id: {}, paint not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Paint not found");
        }
        paintRepository.deleteById(id);
        return new ResponseEntity<>("Paint has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Paint> updatePaint(@PathVariable("id") long id, @RequestBody Paint paint) {
        log.info("Trying to update paint with id: {}", id);
        Optional<Paint> optionalPaint = paintRepository.findById(id);
        if (optionalPaint.isPresent()) {
            log.info("Updating paint with id: {}", id);
            Paint _paint = optionalPaint.get();
            _paint.setSurfacePrice(paint.getSurfacePrice());
            _paint.setType(paint.getType());
            return new ResponseEntity<Paint>(paintRepository.save(_paint), HttpStatus.OK);
        } else {
            log.info("Paint with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Paint>> getAllPaints() {
        log.info("Trying to fetch paint data");
        List<Paint> paintList = new ArrayList<>();
        paintRepository.findAll().forEach(paintList::add);
        if (paintList.isEmpty()) {
            log.info("Unable to fetch any paint data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No paints in database");
        }
        return ResponseEntity.ok().body(paintList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Paint> findPaintById(@PathVariable long id) {
        log.info("Trying to fetch paint data with id: {}", id);
        Optional<Paint> optionalPaint = paintRepository.findById(id);
        if (optionalPaint.isEmpty()) {
            log.info("Unable to fetch paint data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Paint not found");
        }
        return ResponseEntity.ok().body(optionalPaint.get());
    }

}
