package com.zenika.academy.woodblocktoys.Paint;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaintRepository extends CrudRepository<Paint, Long> {

    Paint findByType(String paintChoice);
}
