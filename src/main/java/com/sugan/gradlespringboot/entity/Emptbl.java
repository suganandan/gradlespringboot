package com.sugan.gradlespringboot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Emptbl {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "emptbl_empid_seq", allocationSize = 1)
    private Long empid;
    private String name;
    private String designation;
    private Long age;
}
