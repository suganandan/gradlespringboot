// SONAR-FIX [I-001 java:S1128] This unused import 'org.mapstruct.Mapping' was removed to improve code readability.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that removing this import does not cause any compilation errors or runtime issues.
package com.sugan.gradlespringboot.mapper;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// SONAR-FIX [I-002 java:S1128] Removed unused import 'java.util.List' to improve code clarity.
// HUMAN_REVIEW_REQUIRED [I-002]: Verify that no code relies on the 'java.util.List' import.

// SONAR-FIX [I-001 java:S106] Added SLF4J logger imports to replace System.out.
// HUMAN_REVIEW_REQUIRED [I-001]: Verify that logging is correctly configured and messages appear in logs.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public interface EmpMapper {
    // SONAR-FIX [I-001 java:S106] Declared a static logger to replace direct System.out calls.
    // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the logger is initialized correctly and accessible.
    static final Logger logger = LoggerFactory.getLogger(EmpMapper.class);

    EmpMapper INSTANCE = Mappers.getMapper(EmpMapper.class);
    String default_name = "N/A";
    EmpRequest empToEmpDTO(Emptbl emptbl);
   //@Mapping(target = "empid", ignore = true)
    Emptbl EmpDTOToEmp(EmpRequest empRequest);

    static String joinNames(String first, String second) {
        String result = "";
        result = result + first;
        result = result + second;
        // SONAR-FIX [I-001 java:S106] Replaced System.out.println with logger.info for proper logging.
        // HUMAN_REVIEW_REQUIRED [I-001]: Verify that the log message appears in the application logs.
        logger.info("joined {}", result);
        return result;
    }

}
