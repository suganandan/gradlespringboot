// SONAR-FIX [I-002 java:S1128] Removed unused import statement for java.util.List.
// HUMAN_REVIEW_REQUIRED [I-002]: Verify that java.util.List is not used anywhere in the file.
package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
// SONAR-FIX [I-001 java:S106] Added import for Logger.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logger import is used.
import org.slf4j.Logger;
// SONAR-FIX [I-001 java:S106] Added import for LoggerFactory.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logger factory import is used.
import org.slf4j.LoggerFactory;

@Mapper
public interface EmpMapper {
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
    String default_name = "N/A";
    // SONAR-FIX [I-001 java:S106] Replaced System.out with a static logger for proper logging.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the logger is initialized correctly and used appropriately.
    Logger log = LoggerFactory.getLogger(EmpMapper.class);
    EmpRequest empToEmpDTO(Emptbl emptbl);
    //@Mapping(target = "empid", ignore = true)
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

    static String joinNames(String first, String second) {
        String result = "";
        result = result + first;
        result = result + second;
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with logger.info for structured logging.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the log message is clear and provides necessary information.
        log.info("joined {}", result);
        return result;
    }

}
