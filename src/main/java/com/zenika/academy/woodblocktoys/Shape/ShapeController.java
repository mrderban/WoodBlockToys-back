package com.zenika.academy.woodblocktoys.Shape;

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
@RequestMapping(path = "/shapes")
public class ShapeController {


    /************************VARIABLES & CONSTRUCTOR************************/
    //variables
    private final ShapeRepository shapeRepository;

    //constructor w/ dependency injection
    public ShapeController(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }


    /************************POST & DEL & PUT************************/
    @PostMapping(path = "/", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Shape> createShape(@Valid @RequestBody Shape shape) {
        log.info("Shape registration submitted " + shape.toString());
        try {
            Shape resultShape = shapeRepository.save(shape);
            return ResponseEntity.ok(resultShape);
        } catch (EntityExistsException ex) {
            return ResponseEntity.status((HttpStatus.CONFLICT)).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteShape(@PathVariable("id") long id) {
        log.info("Trying to delete shape with id: {}", id);
        Optional<Shape> optionalShape = shapeRepository.findById(id);
        if (optionalShape.isEmpty()) {
            log.info("Unable to delete shape data with id: {}, shape not found", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Shape not found");
        }
        shapeRepository.deleteById(id);
        return new ResponseEntity<>("Shape has been deleted!", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Shape> updateShape(@PathVariable("id") long id, @RequestBody Shape shape) {
        log.info("Trying to update shape with id: {}", id);
        Optional<Shape> optionalShape = shapeRepository.findById(id);
        if (optionalShape.isPresent()) {
            log.info("Updating shape with id: {}", id);
            Shape _shape = optionalShape.get();
            _shape.setType(shape.getType());
            return new ResponseEntity<Shape>(shapeRepository.save(_shape), HttpStatus.OK);
        } else {
            log.info("Shape with id: {} wasn't found !", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    /************************GET************************/
    @GetMapping(path = "/list", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Shape>> getAllShapes() {
        log.info("Trying to fetch shape data");
        List<Shape> shapeList = new ArrayList<>();
        shapeRepository.findAll().forEach(shapeList::add);
        if (shapeList.isEmpty()) {
            log.info("Unable to fetch any shape data");
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No shapes in database");
        }
        return ResponseEntity.ok().body(shapeList);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Shape> findShapeById(@PathVariable long id) {
        log.info("Trying to fetch shape data with id: {}", id);
        Optional<Shape> optionalShape = shapeRepository.findById(id);
        if (optionalShape.isEmpty()) {
            log.info("Unable to fetch shape data with id: {}", id);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Shape not found");
        }
        return ResponseEntity.ok().body(optionalShape.get());
    }

}
