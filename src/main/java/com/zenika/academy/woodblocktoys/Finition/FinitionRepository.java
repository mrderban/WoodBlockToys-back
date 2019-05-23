package com.zenika.academy.woodblocktoys.Finition;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinitionRepository extends CrudRepository<Finition, Long> {

    Finition findByType(String paintChoice);
}
