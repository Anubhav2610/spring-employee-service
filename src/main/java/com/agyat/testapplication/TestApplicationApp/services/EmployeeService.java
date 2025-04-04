package com.agyat.testapplication.TestApplicationApp.services;


import com.agyat.testapplication.TestApplicationApp.dto.EmployeeDto;
import org.springframework.stereotype.Service;


public interface EmployeeService {

    EmployeeDto getEmployeeById(Long id);
    EmployeeDto createNewEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
    void deleteEmployee(Long id);
}
