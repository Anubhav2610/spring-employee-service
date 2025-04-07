package com.agyat.testapplication.TestApplicationApp.controllers;

import com.agyat.testapplication.TestApplicationApp.dto.EmployeeDto;
import com.agyat.testapplication.TestApplicationApp.entities.Employee;
import com.agyat.testapplication.TestApplicationApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;




class EmployeeControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    EmployeeRepository employeeRepository;


    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }


    @Test
    void testGetEmployeeById_success(){
        Employee savedEmployee = employeeRepository.save(employee);

        webTestClient.get()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
//                .isEqualTo(employeeDto);
//                .value(employeeDto1 -> {
//                    assertThat(employeeDto1.getEmail()).isEqualTo(employeeDto.getEmail());
//                    assertThat(employeeDto1.getId()).isEqualTo(employeeDto.getId());
//                });
    }

    @Test
    void testGetEmployeeById_failure(){
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException(){
        Employee savedEmployee = employeeRepository.save(employee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee(){

        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(employeeDto.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException(){
        Employee savedEmployee  = employeeRepository.save(employee);
        employeeDto.setName("Random");
        employeeDto.setEmail("random@gmail.com");


        webTestClient.put()
                .uri("/employees/{id}" , savedEmployee.getId())
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee(){
        Employee savedEmployee  = employeeRepository.save(employee);
        employeeDto.setName("Random");
        employeeDto.setSalary(250L);


        webTestClient.put()
                .uri("/employees/{id}" , savedEmployee.getId())
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(employeeDto);

    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        Employee savedEmployee  = employeeRepository.save(employee);

        webTestClient.delete()
                .uri("/employees/{id}" , savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/{id}" , savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}