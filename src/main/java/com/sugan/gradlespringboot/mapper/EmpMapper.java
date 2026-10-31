// SONAR-FIX [I-001 java:S1128] This unused import 'org.mapstruct.Mapping' was removed to improve code readability.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that removing this import does not cause any compilation errors or runtime issues.
package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface EmpMapper {
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
    EmpRequest empToEmpDTO(Emptbl emptbl);
   //@Mapping(target = "empid", ignore = true)
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

}
