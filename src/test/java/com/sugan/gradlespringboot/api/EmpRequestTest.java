package com.sugan.gradlespringboot.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmpRequestTest {

    private EmpRequest empRequest;

    @BeforeEach
    void setUp() {
        // Reset requestCounter before each test to ensure independence
        EmpRequest.requestCounter = 0;
        empRequest = new EmpRequest("John Doe", "Developer", 30L, 1L);
    }

    @Test
    @DisplayName("Should create EmpRequest with all arguments and verify getters")
    void shouldCreateEmpRequestAndVerifyGetters() {
        assertThat(empRequest.getName()).isEqualTo("John Doe");
        assertThat(empRequest.getDesignation()).isEqualTo("Developer");
        assertThat(empRequest.getAge()).isEqualTo(30L);
        assertThat(empRequest.getEmpid()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void shouldSetAndGetName() {
        empRequest.setName("Jane Smith");
        assertThat(empRequest.getName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should set and get designation correctly")
    void shouldSetAndGetDesignation() {
        empRequest.setDesignation("Architect");
        assertThat(empRequest.getDesignation()).isEqualTo("Architect");
    }

    @Test
    @DisplayName("Should set and get age correctly")
    void shouldSetAndGetAge() {
        empRequest.setAge(25L);
        assertThat(empRequest.getAge()).isEqualTo(25L);
    }

    @Test
    @DisplayName("Should set and get empid correctly")
    void shouldSetAndGetEmpid() {
        empRequest.setEmpid(2L);
        assertThat(empRequest.getEmpid()).isEqualTo(2L);
    }

    @Test
    @DisplayName("checkAdult should return true when age is 18 or greater")
    void checkAdult_shouldReturnTrue_whenAgeIsAdult() {
        // Test with age = 18 (boundary)
        empRequest.setAge(18L);
        assertThat(empRequest.checkAdult()).isTrue();

        // Test with age > 18
        empRequest.setAge(25L);
        assertThat(empRequest.checkAdult()).isTrue();
    }

    @Test
    @DisplayName("checkAdult should return false when age is less than 18")
    void checkAdult_shouldReturnFalse_whenAgeIsMinor() {
        empRequest.setAge(17L);
        assertThat(empRequest.checkAdult()).isFalse();
    }

    @Test
    @DisplayName("checkAdult should return false when age is null")
    void checkAdult_shouldReturnFalse_whenAgeIsNull() {
        empRequest.setAge(null);
        assertThat(empRequest.checkAdult()).isFalse();
    }

    @Test
    @DisplayName("sameName should return true when names are identical")
    void sameName_shouldReturnTrue_whenNamesAreIdentical() {
        assertThat(empRequest.sameName("John Doe")).isTrue();
    }

    @Test
    @DisplayName("sameName should return false when names are different")
    void sameName_shouldReturnFalse_whenNamesAreDifferent() {
        assertThat(empRequest.sameName("Jane Doe")).isFalse();
    }

    @Test
    @DisplayName("sameName should return true when both names are null")
    void sameName_shouldReturnTrue_whenBothNamesAreNull() {
        empRequest.setName(null);
        assertThat(empRequest.sameName(null)).isTrue();
    }

    @Test
    @DisplayName("sameName should return false when empRequest name is null and other is not")
    void sameName_shouldReturnFalse_whenEmpRequestNameIsNullAndOtherIsNotNull() {
        empRequest.setName(null);
        assertThat(empRequest.sameName("John Doe")).isFalse();
    }

    @Test
    @DisplayName("sameName should return false when empRequest name is not null and other is null")
    void sameName_shouldReturnFalse_whenEmpRequestNameIsNotNullAndOtherIsNull() {
        assertThat(empRequest.sameName(null)).isFalse();
    }

    @Test
    @DisplayName("label should return name in uppercase when upper is true")
    void label_shouldReturnUpperCaseName_whenUpperIsTrue() {
        assertThat(empRequest.label(true, 0)).isEqualTo("JOHN DOE");
    }

    @Test
    @DisplayName("label should return name as is when upper is false")
    void label_shouldReturnNameAsIs_whenUpperIsFalse() {
        assertThat(empRequest.label(false, 0)).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("label should throw NullPointerException when name is null and upper is true")
    void label_shouldThrowNPE_whenNameIsNullAndUpperIsTrue() {
        empRequest.setName(null);
        assertThrows(NullPointerException.class, () -> empRequest.label(true, 0));
    }

    @Test
    @DisplayName("label should return null when name is null and upper is false")
    void label_shouldReturnNull_whenNameIsNullAndUpperIsFalse() {
        empRequest.setName(null);
        assertThat(empRequest.label(false, 0)).isNull();
    }

    @Test
    @DisplayName("requestCounter should be incremented and reflect changes")
    void requestCounter_shouldBeIncremented() {
        assertThat(EmpRequest.requestCounter).isEqualTo(0); // Initial state from @BeforeEach
        EmpRequest.requestCounter++;
        assertThat(EmpRequest.requestCounter).isEqualTo(1);
    }

    @Test
    @DisplayName("equals should return true for same object reference")
    void equals_shouldReturnTrue_forSameObject() {
        EmpRequest sameEmpRequest = empRequest;
        assertThat(empRequest.equals(sameEmpRequest)).isTrue();
    }

    @Test
    @DisplayName("equals should return true for identical objects")
    void equals_shouldReturnTrue_forIdenticalObjects() {
        EmpRequest anotherEmpRequest = new EmpRequest("John Doe", "Developer", 30L, 1L);
        assertThat(empRequest.equals(anotherEmpRequest)).isTrue();
    }

    @Test
    @DisplayName("equals should return false for null object")
    void equals_shouldReturnFalse_forNullObject() {
        assertThat(empRequest.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals should return false for different class type")
    void equals_shouldReturnFalse_forDifferentClass() {
        Object differentObject = new Object();
        assertThat(empRequest.equals(differentObject)).isFalse();
    }

    @Test
    @DisplayName("equals should return false for objects with different name")
    void equals_shouldReturnFalse_forDifferentName() {
        EmpRequest differentEmpRequest = new EmpRequest("Jane Doe", "Developer", 30L, 1L);
        assertThat(empRequest.equals(differentEmpRequest)).isFalse();
    }

    @Test
    @DisplayName("equals should return false for objects with different designation")
    void equals_shouldReturnFalse_forDifferentDesignation() {
        EmpRequest differentEmpRequest = new EmpRequest("John Doe", "Architect", 30L, 1L);
        assertThat(empRequest.equals(differentEmpRequest)).isFalse();
    }

    @Test
    @DisplayName("equals should return false for objects with different age")
    void equals_shouldReturnFalse_forDifferentAge() {
        EmpRequest differentEmpRequest = new EmpRequest("John Doe", "Developer", 31L, 1L);
        assertThat(empRequest.equals(differentEmpRequest)).isFalse();
    }

    @Test
    @DisplayName("equals should return false for objects with different empid")
    void equals_shouldReturnFalse_forDifferentEmpid() {
        EmpRequest differentEmpRequest = new EmpRequest("John Doe", "Developer", 30L, 2L);
        assertThat(empRequest.equals(differentEmpRequest)).isFalse();
    }

    @Test
    @DisplayName("hashCode should be consistent for the same object")
    void hashCode_shouldBeConsistent() {
        int initialHashCode = empRequest.hashCode();
        assertThat(empRequest.hashCode()).isEqualTo(initialHashCode);
    }

    @Test
    @DisplayName("hashCode should be equal for identical objects")
    void hashCode_shouldBeEqual_forIdenticalObjects() {
        EmpRequest anotherEmpRequest = new EmpRequest("John Doe", "Developer", 30L, 1L);
        assertThat(empRequest.hashCode()).isEqualTo(anotherEmpRequest.hashCode());
    }

    @Test
    @DisplayName("hashCode should be different for different objects")
    void hashCode_shouldBeDifferent_forDifferentObjects() {
        EmpRequest differentEmpRequest = new EmpRequest("Jane Doe", "Developer", 30L, 1L);
        assertThat(empRequest.hashCode()).isNotEqualTo(differentEmpRequest.hashCode());
    }

    @Test
    @DisplayName("toString should contain all field values")
    void toString_shouldContainAllFieldValues() {
        String empRequestString = empRequest.toString();
        assertThat(empRequestString)
                .contains("name=John Doe")
                .contains("designation=Developer")
                .contains("age=30")
                .contains("empid=1");
    }
}
