package com.sugan.gradlespringboot.repository;

import com.sugan.gradlespringboot.entity.Emptbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpRepository extends JpaRepository<Emptbl, Long>{
}
