package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmpMapperTest {

    @Test
    @DisplayName("Should ensure mapper instance is initialized")
    void shouldInitializeMapperInstance() {
        // Accessing EmpMapper.INSTANCE triggers its initialization, covering line 12.
        assertThat(EmpMapper.INSTANCE).isNotNull();
    }

    @Test
    @DisplayName("Should correctly join two names and log the result")
    void shouldJoinNamesCorrectly() {
        String firstName = "John";
        String secondName = "Doe";
        String expected = "JohnDoe";

        // Calling joinNames covers lines 22-26, including the logger usage on line 15.
        String result = EmpMapper.joinNames(firstName, secondName);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should map Emptbl to EmpRequest correctly")
    void shouldMapEmptblToEmpRequest() {
        Emptbl emptbl = new Emptbl();
        emptbl.setEmpid(1L);
        emptbl.setName("Alice");
        emptbl.setDesignation("Engineer");
        emptbl.setAge(30L);

        // Calling empToEmpDTO covers line 17.
        EmpRequest empRequest = EmpMapper.INSTANCE.empToEmpDTO(emptbl);

        assertThat(empRequest).isNotNull();
        assertThat(empRequest.getEmpid()).isEqualTo(emptbl.getEmpid());
        assertThat(empRequest.getName()).isEqualTo(emptbl.getName());
        assertThat(empRequest.getDesignation()).isEqualTo(emptbl.getDesignation());
        assertThat(empRequest.getAge()).isEqualTo(emptbl.getAge());
    }

    @Test
    @DisplayName("Should return null when mapping a null Emptbl to EmpRequest")
    void shouldReturnNullWhenMappingNullEmptblToEmpRequest() {
        EmpRequest empRequest = EmpMapper.INSTANCE.empToEmpDTO(null);
        assertThat(empRequest).isNull();
    }

    @Test
    @DisplayName("Should map EmpRequest to Emptbl correctly")
    void shouldMapEmpRequestToEmptbl() {
        EmpRequest empRequest = new EmpRequest("Bob", "Manager", 45L, 2L);

        // Calling EmpDTOToEmp covers line 19.
        Emptbl emptbl = EmpMapper.INSTANCE.EmpDTOToEmp(empRequest);

        assertThat(emptbl).isNotNull();
        assertThat(emptbl.getEmpid()).isEqualTo(empRequest.getEmpid());
        assertThat(emptbl.getName()).isEqualTo(empRequest.getName());
        assertThat(emptbl.getDesignation()).isEqualTo(empRequest.getDesignation());
        assertThat(emptbl.getAge()).isEqualTo(empRequest.getAge());
    }

    @Test
    @DisplayName("Should return null when mapping a null EmpRequest to Emptbl")
    void shouldReturnNullWhenMappingNullEmpRequestToEmptbl() {
        Emptbl emptbl = EmpMapper.INSTANCE.EmpDTOToEmp(null);
        assertThat(emptbl).isNull();
    }
}
