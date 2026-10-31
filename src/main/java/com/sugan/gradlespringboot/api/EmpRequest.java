// SONAR-FIX [I-003 java:S1128] Removed unused import 'java.util.Date'.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that 'java.util.Date' is indeed not used anywhere in the file.
// SONAR-FIX [I-004 java:S1128] Removed unused import 'java.util.Map'.
// HUMAN_REVIEW_REQUIRED [I-004]: Verify that 'java.util.Map' is indeed not used anywhere in the file.
package com.sugan.gradlespringboot.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@Schema
public class EmpRequest {
    public static int requestCounter = 0;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private String name;
    @NotBlank(message = "Designation cannot be blank")
    @Size(min = 2, max = 20, message = "Designation must be between 2 and 20 characters")
    private String designation;
    @NotNull(message = "age cannot be null")
    private Long age;
    private Long empid;

    public boolean checkAdult() {
        // SONAR-FIX [I-005 java:S1125] Removed redundant boolean literal.
        // HUMAN_REVIEW_REQUIRED [I-005]: Verify that the simplified boolean expression maintains the original logic.
        return age != null && age >= 18;
    }

    public boolean sameName(String other) {
        // SONAR-FIX [I-001 java:S4973] Changed string comparison from '==' to 'equals()' for correct value comparison.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that comparing 'name' with 'other' using 'equals()' is the intended behavior, especially considering nullability.
        return name != null && name.equals(other);
    }

    // SONAR-FIX [I-002 java:S1172] Removed unused method parameter "unusedFlag".
    // HUMAN_REVIEW_REQUIRED [I-002]: Verify that the removal of 'unusedFlag' parameter does not break any external calls to this method.
    public String label(boolean upper) {
        String result = name;
        if (upper) {
            result = name.toUpperCase();
        }
        // SONAR-FIX [I-006 java:S1858] Removed redundant 'toString()' call on an already String object.
        // HUMAN_REVIEW_REQUIRED [I-006]: Verify that returning 'result' directly is correct, as it is already a String.
        return result;
    }
}
