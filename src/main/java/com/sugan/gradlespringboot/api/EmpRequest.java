// SONAR-FIX [I-003 java:S1128] Removed unused import 'java.util.Date'.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify that java.util.Date is indeed not used anywhere in the file.
// SONAR-FIX [I-004 java:S1128] Removed unused import 'java.util.Map'.
// HUMAN_REVIEW_REQUIRED [I-004]: Verify that java.util.Map is indeed not used anywhere in the file.
package com.sugan.gradlespringboot.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
        // SONAR-FIX [I-005 java:S1125] Removed redundant boolean literal by simplifying the ternary operator.
        // HUMAN_REVIEW_REQUIRED [I-005]: Verify that the boolean logic remains the same.
        return age != null && age >= 18;
    }

    public boolean sameName(String other) {
        // SONAR-FIX [I-001 java:S4973] Compared String objects using Objects.equals() for null-safe value comparison.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that comparing string values is the intended behavior.
        return Objects.equals(name, other);
    }

    // SONAR-FIX [I-002 java:S1172] Removed unused method parameter 'unusedFlag'.
    // HUMAN_REVIEW_REQUIRED [I-002]: Verify that 'unusedFlag' is not needed for any future functionality.
    public String label(boolean upper) {
        String result = name;
        if (upper) {
            result = name.toUpperCase();
        }
        // SONAR-FIX [I-006 java:S1858] Removed redundant toString() call on a String object.
        // HUMAN_REVIEW_REQUIRED [I-006]: Verify that returning the String directly does not change behavior.
        return result;
    }
}
