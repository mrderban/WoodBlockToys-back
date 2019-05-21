package com.zenika.academy.woodblocktoys.Shape;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShapeRepository extends CrudRepository<Shape, Long> {

}
