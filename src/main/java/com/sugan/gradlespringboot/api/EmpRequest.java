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
        return age != null && age >= 18;
    }

    public boolean sameName(String other) {
        return Objects.equals(name, other);
    }

    public String label(boolean upper, int unusedFlag) {
        String result = name;
        if (upper) {
            result = name.toUpperCase();
        }
        return result;
    }
}
