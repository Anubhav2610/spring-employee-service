package com.agyat.testapplication.TestApplicationApp.services.impl;

import com.agyat.testapplication.TestApplicationApp.TestContainerConfiguration;
import com.agyat.testapplication.TestApplicationApp.dto.EmployeeDto;
import com.agyat.testapplication.TestApplicationApp.entities.Employee;
import com.agyat.testapplication.TestApplicationApp.exceptions.ResourceNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
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
    void testGetEmployeeById_WhenEmployeeIsNotPresent_ThenThrowException(){
//        arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());


//        act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

         verify(employeeRepository).findById(1L);

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

    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenThrowException(){
//        Assign
        when(employeeRepository.findByEmail(mockEmployee.getEmail())).thenReturn(List.of(mockEmployee));

//        Act and Assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(mockEmployee.getEmail());
        verify(employeeRepository , never()).save(any());
    }


    @Test
    void testUpdateEmployee_whenEmployeeNotExists_ThenThrowException(){
//        assign
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(1L , mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository , never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_ThenThrowException(){
//        assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));
        mockEmployee.setName("Random");
        mockEmployee.setEmail("random@gmail.com");

//        act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId() , mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository , never()).save(any());

    }

    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployee(){
//        Assign
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setSalary(1494L);
        Employee newEmployee = modelMapper.map(mockEmployeeDto ,Employee.class);

        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);
//        act and assert

        EmployeeDto updateEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId() , mockEmployeeDto);

       assertThat(updateEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());
    }

    @Test
    void testDeleteEmployee_WhenEmployeeNotExists_ThenThrowException(){
//        assign
        when(employeeRepository.existsById(1L )).thenReturn(false);

//        act and assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).existsById(anyLong());
        verify(employeeRepository , never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_WhenEmployeeIsValid_ThenDeleteEmployee(){
//        assign
        when(employeeRepository.existsById(1L)).thenReturn(true);

//        act and assert

        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);
    }

}