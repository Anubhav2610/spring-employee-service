package com.agyat.testapplication.TestApplicationApp.controllers;

import com.agyat.testapplication.TestApplicationApp.TestContainerConfiguration;
import com.agyat.testapplication.TestApplicationApp.dto.EmployeeDto;
import com.agyat.testapplication.TestApplicationApp.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@AutoConfigureWebTestClient(timeout = "100000")
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;


    Employee employee = Employee.builder()
            .email("agyat@gmail.com")
                .salary(200L)
                .name("agyat")
                .build();

    EmployeeDto employeeDto = EmployeeDto.builder()
            .email("agyat@gmail.com")
                .salary(200L)
                .name("agyat")
                .build();

}
