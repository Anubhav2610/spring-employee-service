package com.agyat.testapplication.TestApplicationApp.repositories;

import com.agyat.testapplication.TestApplicationApp.TestContainerConfiguration;
import com.agyat.testapplication.TestApplicationApp.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestContainerConfiguration.class)
@DataJpaTest
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        employee = Employee.builder()
                .email("agyat123@gmail.com")
                .name("Agyat")
                .salary(100)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        //given , arrange
        employeeRepository.save(employee);

        //when , act
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //then , assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());

    }

    @Test
    void testFindByEmail_whenEmailNotFound_thenReturnEmptyList(){
        //given
        String email = "notpresent@gmail.com";

        //when

        List<Employee> employeeList = employeeRepository.findByEmail(email);

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
    }
}