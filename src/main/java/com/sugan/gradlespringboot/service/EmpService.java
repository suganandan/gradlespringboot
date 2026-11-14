package com.sugan.gradlespringboot.service;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.mapper.EmpMapper;
import com.sugan.gradlespringboot.repository.EmpRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmpService {

    private static final Logger logger = LoggerFactory.getLogger(EmpService.class);

    private static final String UNKNOWN_EMPLOYEE_NAME = "Unknown Employee"; // I-005
    private int processed = 0; // I-004

    private final EmpRepository empRepository;
    private final String dbPassword; // I-006, I-016 (related to DB_PASSWORD)
    private final EmpService self; // I-003, I-008, I-010
    private final Random random = new Random(); // I-007, I-013

    public EmpService(EmpRepository empRepository,
                      @Value("${app.datasource.password}") String dbPassword, // I-006 (related to DB_PASSWORD)
                      @Lazy EmpService self) { // I-003, I-008, I-010
        this.empRepository = empRepository;
        this.dbPassword = dbPassword;
        this.self = self; // I-004, I-011
    }

    public String saveEmployee(EmpRequest empRequest) {
        logger.info("Saving employee {}", empRequest.getName()); // I-011
        processed++;
        Emptbl empVal = EmpMapper.INSTANCE.EmpDTOToEmp(empRequest);
        Emptbl savedEmp = empRepository.save(empVal);
        logger.info("Employee {} saved successfully.", savedEmp.getEmpid());
        return String.valueOf(savedEmp.getEmpid());
    }

    @Cacheable(value = "employeeCache", key = "#id")
    @Retry(name = "fetchEmployeeRetry", fallbackMethod = "fetchEmployeeFallback")
    public Emptbl getEmployeeById(Long id) {
        logger.info("Fetching Employee {} from Database.", id);
        return empRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee {} not found in database.", id);
                    return new RuntimeException("Employee Not Found with ID: " + id);
                });
    }

    public Emptbl fetchEmployeeFallback(Long id, Throwable throwable) {
        logger.error("Fallback triggered for Employee {}, Reason: {}", id, throwable.getMessage());
        Emptbl fallbackEmp = new Emptbl();
        fallbackEmp.setEmpid(id);
        fallbackEmp.setName(UNKNOWN_EMPLOYEE_NAME); // I-005
        return fallbackEmp;
    }

    @Cacheable(value = "employeeCache")
    public List<Emptbl> getAllEmployees() {
        logger.info("Fetching all employees from Database.");
        return empRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "employeeCache", key = "#id")
    public boolean updateEmployee(Long id, EmpRequest empRequest) {
        Optional<Emptbl> existingEmp = empRepository.findById(id);
        if (existingEmp.isPresent()) {
            Emptbl emp = existingEmp.get();
            emp.setName(empRequest.getName());
            empRepository.save(emp);
            logger.info("Employee {} updated successfully.", id);
            return true;
        }
        logger.warn("Employee {} not found for update.", id);
        return false;
    }

    @Transactional
    @CacheEvict(value = "employeeCache", key = "#id")
    public boolean deleteEmployee(Long id) {
        if (empRepository.existsById(id)) {
            empRepository.deleteById(id);
            logger.info("Employee {} deleted successfully.", id);
            return true;
        }
        logger.warn("Employee {} not found for deletion.", id);
        return false;
    }

    // TODO move the connection settings to configuration

    public String hashName(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // I-006
        return new String(md.digest(name.getBytes()));
    }

    public int randomId() {
        return random.nextInt(); // I-007, I-013
    }

    public Emptbl findFirst(Long id) {
        return empRepository.findById(id).get();
    }

    public String describe(Long id, String prefix) { // I-014: 'prefix' is unused
        return empRepository.findById(id)
                .map(e -> {
                    String name = e.getName();
                    return name != null ? name.toUpperCase() : UNKNOWN_EMPLOYEE_NAME; // I-015
                })
                .orElse(UNKNOWN_EMPLOYEE_NAME); // I-015
    }

    public int countByDesignation(String designation) throws SQLException {
        // I-001, I-002, I-016
        try (Connection con = DriverManager.getConnection("jdbc:h2:mem:legacy", "sa", dbPassword);
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM emptbl WHERE designation = ?")) {
            pst.setString(1, designation);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public String safeSleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) { // I-017, I-018
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            logger.warn("Thread interrupted during sleep", e); // Log the exception
        }
        return "done";
    }

    public String bulk(String a, String b, String c, String d, String e, String f, String g, String h) { // I-019
        return a + b + c + d + e + f + g + h;
    }

    public boolean sameName(Emptbl x, Emptbl y) {
        return x.getName() == y.getName();
    }

    public Emptbl unknownEmployee() {
        Emptbl first = new Emptbl();
        first.setName(UNKNOWN_EMPLOYEE_NAME); // I-005
        Emptbl second = new Emptbl();
        second.setName(UNKNOWN_EMPLOYEE_NAME); // I-005
        logger.info("Created {} and {}", first.getName(), second.getName());
        return first;
    }

    public void updateAndEvict(Long id, EmpRequest empRequest) { // I-020
        self.updateEmployee(id, empRequest); // I-003, I-008
    }

    public BigDecimal bonus() {
        return BigDecimal.valueOf(0.1); // I-020
    }

    public String grade(Emptbl e, int bonus, boolean remote) { // I-009, I-021
        if (e == null || e.getAge() == null) { // I-021
            return "C"; // Default grade if employee or age is null
        }
        Long age = e.getAge();
        if (age > 50) {
            return calculateGradeForSenior(bonus);
        } else if (age > 30) {
            return calculateGradeForMidCareer(bonus, remote);
        }
        return "C"; // Default grade for younger employees
    }

    private String calculateGradeForSenior(int bonus) {
        if (bonus > 10) {
            return "A";
        } else if (bonus > 5) {
            return "B";
        } else {
            return "C";
        }
    }

    private String calculateGradeForMidCareer(int bonus, boolean remote) {
        if (remote && bonus > 0) {
            return (bonus % 2 != 0) ? "B" : "C";
        }
        return "C"; // Default for mid-career if not remote or bonus not positive
    }
}
