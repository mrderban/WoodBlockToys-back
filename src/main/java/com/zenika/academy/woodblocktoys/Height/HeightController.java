package com.zenika.academy.woodblocktoys.Height;


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
@RequestMapping(path = "/heights")
public class HeightController {


    /************************VARIABLES & CONSTRUCTOR************************/
    //variables
    private final HeightRepository heightRepository;

    //constructor w/ dependency injection
    public HeightController(HeightRepository heightRepository) {
        this.heightRepository = heightRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Height> createHeight(@Valid @RequestBody Height height) {
        log.info("Height registration submitted " + height.toString());
        try {
            Height resultHeight = heightRepository.save(height);
            return ResponseEntity.ok(resultHeight);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteHeight(@PathVariable("id") long id) {
        log.info("Trying to delete height with id: {}", id);
        Optional<Height> optionalHeight = heightRepository.findById(id);
        if (optionalHeight.isEmpty()) {
            log.info("Unable to delete height data with id: {}, height not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Height not found");
        }
        heightRepository.deleteById(id);
        return new ResponseEntity<>("Height has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Height> updateHeight(@PathVariable("id") long id, @RequestBody Height height) {
        log.info("Trying to update height with id: {}", id);
        Optional<Height> optionalHeight = heightRepository.findById(id);
        if (optionalHeight.isPresent()) {
            log.info("Updating height with id: {}", id);
            Height _height = optionalHeight.get();
            _height.setValue(height.getValue());
            return new ResponseEntity<Height>(heightRepository.save(_height), HttpStatus.OK);
        } else {
            log.info("Height with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Height>> getAllHeights() {
        log.info("Trying to fetch height data");
        List<Height> heightList = new ArrayList<>();
        heightRepository.findAll().forEach(heightList::add);
        if (heightList.isEmpty()) {
            log.info("Unable to fetch any height data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No heights in database");
        }
        return ResponseEntity.ok().body(heightList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Height> findHeightById(@PathVariable long id) {
        log.info("Trying to fetch height data with id: {}", id);
        Optional<Height> optionalHeight = heightRepository.findById(id);
        if (optionalHeight.isEmpty()) {
            log.info("Unable to fetch height data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Height not found");
        }
        return ResponseEntity.ok().body(optionalHeight.get());
    }

}
