package com.zenika.academy.woodblocktoys.Wood;

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
@RequestMapping(path = "/woods")
public class WoodController {


    /************************VARIABLES & CONSTRUCTOR************************/

    private final WoodRepository woodRepository;

    public WoodController(WoodRepository woodRepository) {
        this.woodRepository = woodRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Wood> createWood(@Valid @RequestBody Wood wood) {
        log.info("Wood registration submitted " + wood.toString());
        try {
            Wood resultWood = woodRepository.save(wood);
            return ResponseEntity.ok(resultWood);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteWood(@PathVariable("id") long id) {
        log.info("Trying to delete wood with id: {}", id);
        Optional<Wood> optionalWood = woodRepository.findById(id);
        if (optionalWood.isEmpty()) {
            log.info("Unable to delete wood data with id: {}, wood not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wood not found");
        }
        woodRepository.deleteById(id);
        return new ResponseEntity<>("Wood has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Wood> updateWood(@PathVariable("id") long id, @RequestBody Wood wood) {
        log.info("Trying to update wood with id: {}", id);
        Optional<Wood> optionalWood = woodRepository.findById(id);
        if (optionalWood.isPresent()) {
            log.info("Updating wood with id: {}", id);
            Wood _wood = optionalWood.get();
            _wood.setVolumePrice(wood.getVolumePrice());
            _wood.setType(wood.getType());
            return new ResponseEntity<Wood>(woodRepository.save(_wood), HttpStatus.OK);
        } else {
            log.info("Wood with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Wood>> getAllWoods() {
        log.info("Trying to fetch wood data");
        List<Wood> woodList = new ArrayList<>();
        woodRepository.findAll().forEach(woodList::add);
        if (woodList.isEmpty()) {
            log.info("Unable to fetch any wood data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No woods in database");
        }
        return ResponseEntity.ok().body(woodList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Wood> findWoodById(@PathVariable long id) {
        log.info("Trying to fetch wood data with id: {}", id);
        Optional<Wood> optionalWood = woodRepository.findById(id);
        if (optionalWood.isEmpty()) {
            log.info("Unable to fetch wood data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Wood not found");
        }
        return ResponseEntity.ok().body(optionalWood.get());
    }

}
