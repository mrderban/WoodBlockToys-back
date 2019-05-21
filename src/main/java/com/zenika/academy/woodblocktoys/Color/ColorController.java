package com.zenika.academy.woodblocktoys.Color;


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
@RequestMapping(path = "/colors")
public class ColorController {


    /************************VARIABLES & CONSTRUCTOR************************/
    //variables
    private final ColorRepository colorRepository;

    //constructor w/ dependency injection
    public ColorController(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Color> createColor(@Valid @RequestBody Color color) {
        log.info("Color registration submitted " + color.toString());
        try {
            Color resultColor = colorRepository.save(color);
            return ResponseEntity.ok(resultColor);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable("id") long id) {
        log.info("Trying to delete color with id: {}", id);
        Optional<Color> optionalColor = colorRepository.findById(id);
        if (optionalColor.isEmpty()) {
            log.info("Unable to delete color data with id: {}, color not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Color not found");
        }
        colorRepository.deleteById(id);
        return new ResponseEntity<>("Color has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Color> updateColor(@PathVariable("id") long id, @RequestBody Color color) {
        log.info("Trying to update color with id: {}", id);
        Optional<Color> optionalColor = colorRepository.findById(id);
        if (optionalColor.isPresent()) {
            log.info("Updating color with id: {}", id);
            Color _color = optionalColor.get();
            _color.setType(color.getType());
            return new ResponseEntity<Color>(colorRepository.save(_color), HttpStatus.OK);
        } else {
            log.info("Color with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Color>> getAllColors() {
        log.info("Trying to fetch color data");
        List<Color> colorList = new ArrayList<>();
        colorRepository.findAll().forEach(colorList::add);
        if (colorList.isEmpty()) {
            log.info("Unable to fetch any color data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No colors in database");
        }
        return ResponseEntity.ok().body(colorList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Color> findColorById(@PathVariable long id) {
        log.info("Trying to fetch color data with id: {}", id);
        Optional<Color> optionalColor = colorRepository.findById(id);
        if (optionalColor.isEmpty()) {
            log.info("Unable to fetch color data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Color not found");
        }
        return ResponseEntity.ok().body(optionalColor.get());
    }

}
