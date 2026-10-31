package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface EmpMapper {
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
    String default_name = "N/A";
    EmpRequest empToEmpDTO(Emptbl emptbl);
    //@Mapping(target = "empid", ignore = true)
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

    static String joinNames(String first, String second) {
        String result = "";
        result = result + first;
        result = result + second;
        System.out.println("joined " + result);
        return result;
    }

}