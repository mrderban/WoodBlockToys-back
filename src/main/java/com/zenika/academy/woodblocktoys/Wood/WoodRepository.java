package com.zenika.academy.woodblocktoys.Wood;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WoodRepository extends CrudRepository<Wood, Long> {

}
