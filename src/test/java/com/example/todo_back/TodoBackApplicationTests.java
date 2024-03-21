package com.example.todo_back;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TodoBackApplication.class})
class TodoBackApplicationTests {

	@Autowired
	TodoBackApplication todoBackApplication;
	@Test
	void contextLoads() {
		assertTrue(true, "This test is expected to pass.");
	}
}
