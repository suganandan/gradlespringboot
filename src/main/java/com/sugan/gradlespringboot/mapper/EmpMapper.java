package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface EmpMapper {
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);

    EmpRequest empToEmpDTO(Emptbl emptbl);

    Emptbl EmpDTOToEmp(EmpRequest empRequest);

}