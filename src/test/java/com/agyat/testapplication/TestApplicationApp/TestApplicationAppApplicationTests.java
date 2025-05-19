package com.agyat.testapplication.TestApplicationApp;

import lombok.extern.slf4j.Slf4j;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;



class TestApplicationAppApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(TestApplicationAppApplicationTests.class);


	@BeforeEach
	void setUp(){
		log.info("Starting the method , Setting up Config");
	}

	@AfterEach
	void tearDown(){
		log.info("Tearing down the , method");
	}

	@BeforeAll
	static void setUpOnce(){
		log.info("SetUp once...");
	}

	@AfterAll
	static void tearDownOnce(){
		log.info("Tearing down all......");
	}

	@Test
//	@Disabled
	void testNumberOne() {
		int a = 5;
		int b = 3;

		int result = addTwoNumber(a , b);

//		Assertions.assertEquals( 8 , result);

		Assertions.assertThat(result)
				.isEqualTo(8)
				.isCloseTo(8 , Offset.offset(1));
	}


//	@DisplayName("testNameTwo")
	void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException() {
		int a = 5;
		int b = 0;

		//double result  = divideTwoNumbers(a , b);
		assertThatThrownBy(() -> divideTwoNumbers(a , b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Arithmetic Exception occured");
	}

	int addTwoNumber(int a , int b ) {
		return a + b;
	}

	double divideTwoNumbers(int a , int b){
		try {
			return a / b;
		}catch (ArithmeticException e){
			log.error("Arithmetic Exception occured : "+e.getLocalizedMessage());
			throw new ArithmeticException("Arithmetic Exception occured");
		}
	}
}
