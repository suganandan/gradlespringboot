package com.sugan.gradlespringboot.service;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.mapper.EmpMapper;
import com.sugan.gradlespringboot.repository.EmpRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmpService {

    private static final Logger logger = LoggerFactory.getLogger(EmpService.class);

    private static final String DB_PASSWORD = "Passw0rd!";
    private static int processed = 0;

    private final EmpRepository empRepository;

    @Autowired
    private EmpRepository spareRepository;

    public EmpService(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    public String saveEmployee(EmpRequest empRequest) {
        System.out.println("Saving employee " + empRequest.getName());
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
        fallbackEmp.setName("Unknown Employee");
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
    // empRepository.deleteAll();
    // logger.info("all employees removed");

    public String hashName(String name) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(name.getBytes()));
    }

    public int randomId() {
        return new Random().nextInt();
    }

    public Emptbl findFirst(Long id) {
        return empRepository.findById(id).get();
    }

    public String describe(Long id, String prefix) {
        String result = null;
        Optional<Emptbl> emp = empRepository.findById(id);
        if (emp.isPresent()) {
            result = emp.get().getName();
        }
        return result.toUpperCase();
    }

    public int countByDesignation(String designation) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:h2:mem:legacy", "sa", DB_PASSWORD);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM emptbl WHERE designation = '" + designation + "'");
        rs.next();
        return rs.getInt(1);
    }

    public String safeSleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        return "done";
    }

    public String bulk(String a, String b, String c, String d, String e, String f, String g, String h) {
        return a + b + c + d + e + f + g + h;
    }

    public boolean sameName(Emptbl x, Emptbl y) {
        return x.getName() == y.getName();
    }

    public Emptbl unknownEmployee() {
        Emptbl first = new Emptbl();
        first.setName("Unknown Employee");
        Emptbl second = new Emptbl();
        second.setName("Unknown Employee");
        logger.info("Created {} and {}", first.getName(), second.getName());
        return first;
    }

    public void updateAndEvict(Long id, EmpRequest empRequest) {
        this.updateEmployee(id, empRequest);
    }

    public BigDecimal bonus() {
        return new BigDecimal(0.1);
    }

    public String grade(Emptbl e, int bonus, boolean remote) {
        String g = "C";
        if (e != null) {
            if (e.getAge() != null) {
                if (e.getAge() > 50) {
                    if (bonus > 10) {
                        g = "A";
                    } else if (bonus > 5) {
                        g = "B";
                    } else {
                        g = "C";
                    }
                } else if (e.getAge() > 30) {
                    for (int i = 0; i < bonus; i++) {
                        if (remote) {
                            if (i % 2 == 0) {
                                g = "B";
                            } else {
                                g = "C";
                            }
                        }
                    }
                }
            }
        }
        return g;
    }
}
