package com.sugan.gradlespringboot.entity;

import com.sugan.gradlespringboot.listener.AuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter

@ToString
@EntityListeners(AuditListener.class)
public class Emptbl {
    public static final String table_name = "emptbl";
    public static int instanceCount = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "emptbl_empid_seq", allocationSize = 1)
    private Long empid;
    private String name;
    private String designation;
    private Long age;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Emptbl)) {
            return false;
        }
        Emptbl other = (Emptbl) o;
        return empid != null && empid.equals(other.empid);
    }

    public String category() {
        if (age == null) {
            return "unknown";
        }
        if (age > 60) {
            return "senior";
        } else {
            return "senior";
        }
    }

    public String summary() {
        String result = "";
        String unused = "x";
        result = result + name;
        result = result + " - ";
        result = result + designation;
        return result;
    }

}
