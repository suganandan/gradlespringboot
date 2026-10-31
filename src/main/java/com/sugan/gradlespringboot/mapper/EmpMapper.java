package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public interface EmpMapper {
    Logger logger = LoggerFactory.getLogger(EmpMapper.class);
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
    String DEFAULT_NAME = "N/A";
    EmpRequest empToEmpDTO(Emptbl emptbl);
    //@Mapping(target = "empid", ignore = true)
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

    static String joinNames(String first, String second) {
        String result = "";
        result = result + first;
        result = result + second;
        logger.info("joined {}", result);
        return result;
    }

}
