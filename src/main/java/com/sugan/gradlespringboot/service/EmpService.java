package com.sugan.gradlespringboot.service;

import com.sugan.gradlespringboot.api.EmpRequest;
import com.sugan.gradlespringboot.entity.Emptbl;
import com.sugan.gradlespringboot.mapper.EmpMapper;
import com.sugan.gradlespringboot.repository.EmpRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpService {

    private final EmpRepository empRepository;

    @Autowired
    public EmpService(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }


    public String saveEmployee(EmpRequest empRequest) {
        try {
            Emptbl empVal = EmpMapper.INSTANCE.EmpDTOToEmp(empRequest);
            Emptbl updateVal = empRepository.save(empVal);
            return String.valueOf(updateVal.getEmpid());
        } catch (ServiceException exception) {
            throw new ServiceException(exception.getMessage());
        }
    }
}
