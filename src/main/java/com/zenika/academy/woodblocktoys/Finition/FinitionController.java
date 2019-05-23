package com.zenika.academy.woodblocktoys.Finition;


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
@RequestMapping(path = "/finitions")
public class FinitionController {


    /************************VARIABLES & CONSTRUCTOR************************/
    private final FinitionRepository finitionRepository;

    public FinitionController(FinitionRepository finitionRepository) {
        this.finitionRepository = finitionRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Finition> createPaint(@Valid @RequestBody Finition finition) {
        log.info("Finition registration submitted " + finition.toString());
        try {
            Finition resultFinition = finitionRepository.save(finition);
            return ResponseEntity.ok(resultFinition);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deletePaint(@PathVariable("id") long id) {
        log.info("Trying to delete finition with id: {}", id);
        Optional<Finition> optionalPaint = finitionRepository.findById(id);
        if (optionalPaint.isEmpty()) {
            log.info("Unable to delete finition data with id: {}, finition not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Finition not found");
        }
        finitionRepository.deleteById(id);
        return new ResponseEntity<>("Finition has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Finition> updatePaint(@PathVariable("id") long id, @RequestBody Finition finition) {
        log.info("Trying to update finition with id: {}", id);
        Optional<Finition> optionalPaint = finitionRepository.findById(id);
        if (optionalPaint.isPresent()) {
            log.info("Updating finition with id: {}", id);
            Finition _finition = optionalPaint.get();
            _finition.setSurfacePrice(finition.getSurfacePrice());
            _finition.setType(finition.getType());
            return new ResponseEntity<Finition>(finitionRepository.save(_finition), HttpStatus.OK);
        } else {
            log.info("Finition with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Finition>> getAllPaints() {
        log.info("Trying to fetch finition data");
        List<Finition> finitionList = new ArrayList<>();
        finitionRepository.findAll().forEach(finitionList::add);
        if (finitionList.isEmpty()) {
            log.info("Unable to fetch any finition data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No finitions in database");
        }
        return ResponseEntity.ok().body(finitionList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Finition> findPaintById(@PathVariable long id) {
        log.info("Trying to fetch finition data with id: {}", id);
        Optional<Finition> optionalPaint = finitionRepository.findById(id);
        if (optionalPaint.isEmpty()) {
            log.info("Unable to fetch finition data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Finition not found");
        }
        return ResponseEntity.ok().body(optionalPaint.get());
    }

}
