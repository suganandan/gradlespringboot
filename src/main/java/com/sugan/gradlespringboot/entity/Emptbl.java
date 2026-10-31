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

    // SONAR-FIX [I-003 java:S1206] Added hashCode() method to comply with the contract between equals() and hashCode().
    // HUMAN_REVIEW_REQUIRED [I-003]: Verify that the hashCode() implementation correctly reflects the equality logic based on 'empid'.
    @Override
    public int hashCode() {
        return empid != null ? empid.hashCode() : 0;
    }

    public String category() {
        if (age == null) {
            return "unknown";
        }
        // SONAR-FIX [I-001 java:S3923] Simplified conditional structure as both branches returned the same value.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the logic for 'category' is correct and always returns "senior" if age is not null.
        return "senior";
    }

    public String summary() {
        String result = "";
        // SONAR-FIX [I-002 java:S1854] Removed useless assignment to local variable 'unused'.
        // SONAR-FIX [I-004 java:S1481] Removed unused local variable 'unused'.
        // HUMAN_REVIEW_REQUIRED [I-002]: Verify that removing the 'unused' variable does not impact functionality.
        // HUMAN_REVIEW_REQUIRED [I-004]: Verify that removing the 'unused' variable does not impact functionality.
        result = result + name;
        result = result + " - ";
        result = result + designation;
        return result;
    }

}
