package com.agyat.testapplication.TestApplicationApp;

import com.agyat.testapplication.TestApplicationApp.services.DataService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@SpringBootApplication
public class TestApplicationAppApplication implements CommandLineRunner {

	private  final DataService dataService;

	@Value("${my.variable}")
	private String myVariable;

	public static void main(String[] args) {
		SpringApplication.run(TestApplicationAppApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		System.out.println("my variable "+myVariable);
		System.out.println("The Data is : "+dataService.getData());
	}
}
