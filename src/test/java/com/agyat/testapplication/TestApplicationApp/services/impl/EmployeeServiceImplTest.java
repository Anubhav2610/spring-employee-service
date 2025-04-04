package com.agyat.testapplication.TestApplicationApp.services.impl;

import com.agyat.testapplication.TestApplicationApp.TestContainerConfiguration;
import com.agyat.testapplication.TestApplicationApp.dto.EmployeeDto;
import com.agyat.testapplication.TestApplicationApp.entities.Employee;
import com.agyat.testapplication.TestApplicationApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;

    private EmployeeDto mockEmployeeDto;
    @BeforeEach
    void setUp(){
        mockEmployee = Employee.builder()
                .id(1L)
                .name("Agyat")
                .email("agyat@gmail.com")
                .salary(3000L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee , EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        //Assign
        Long id = mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));//Stubbing

        //Act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        //Assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
//        verify(employeeRepository).findById(id);
//        verify(employeeRepository).findByEmail("");
//        verify(employeeRepository , times(1)).findById(id);
//        verify(employeeRepository , atLeast(1)).findById(id);
        verify(employeeRepository , only()).findById(id);
    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_ThenCreateNewEmployee(){
//        Assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);


//        Act

        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

//        Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());

    }
}