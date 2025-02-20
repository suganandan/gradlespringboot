package com.sugan.gradlespringboot.service;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.mapper.EmpMapper;
import com.sugan.gradlespringboot.repository.EmpRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpService {

    private static final Logger logger = LoggerFactory.getLogger(EmpService.class);

    private final EmpRepository empRepository;

    public EmpService(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    public String saveEmployee(EmpRequest empRequest) {
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
}
