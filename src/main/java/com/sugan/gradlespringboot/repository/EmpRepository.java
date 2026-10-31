package com.sugan.gradlespringboot.repository;

import com.sugan.gradlespringboot.entity.Emptbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpRepository extends JpaRepository<Emptbl, Long>{

    default List findRaw() {
        return findAll();
    }

    default boolean isEmptyTable() {
        System.out.println("checking table");
        return findAll().size() == 0;
    }
}
