// SONAR-FIX [I-002 java:S1128] Removed unused import 'java.util.List'.
// HUMAN_REVIEW_REQUIRED [I-002]: Verify no code relies on the removed import.
package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
// SONAR-FIX [I-001 java:S106] Added SLF4J Logger import for logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logging library is used.
import org.slf4j.Logger;
// SONAR-FIX [I-001 java:S106] Added SLF4J LoggerFactory import for logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify the correct logging library is used.
import org.slf4j.LoggerFactory;

@Mapper
public interface EmpMapper {
    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
// SONAR-FIX [I-003 java:S115] Renamed constant to follow naming convention.
// HUMAN_REVIEW_REQUIRED [I-003]: Verify constant name DEFAULT_NAME is appropriate.
    String DEFAULT_NAME = "N/A";
// SONAR-FIX [I-001 java:S106] Added a static logger for proper logging.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify logger instance is correctly initialized.
    static final Logger log = LoggerFactory.getLogger(EmpMapper.class);
    EmpRequest empToEmpDTO(Emptbl emptbl);
    //@Mapping(target = "empid", ignore = true)
    // HUMAN_REVIEW_REQUIRED [java:S100]: Sonar reports: Rename this method name to match the regular expression '^[a-z][a-zA-Z0-9]*$'. Not changed by the agent because the name is used in other files; use the IDE rename refactor.
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

    static String joinNames(String first, String second) {
        String result = "";
        result = result + first;
        result = result + second;
// SONAR-FIX [I-001 java:S106] Replaced System.out.println with logger.info.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify log message content and level are appropriate.
        log.info("joined {}", result);
        return result;
    }

}