package com.siriusxm.demo;

import com.siriusxm.demo.domain.ProcessingResult;
import com.siriusxm.demo.exception.OrderProcessingException;
import com.siriusxm.demo.model.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BeveragesShopApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testProcessStandardPriorityOrder() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setId("req1");
		orderRequest.setName("Test Order");
		orderRequest.setDescription("Sample order for testing");
		orderRequest.setPriority("STD");
		orderRequest.setItems(List.of("Iced Latte", "Green Tea"));

		ResponseEntity<ProcessingResult> response = restTemplate.postForEntity("/api/orders/process", orderRequest, ProcessingResult.class);

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		ProcessingResult result = response.getBody();
		assertThat(result).isNotNull();
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getTotalPrice()).isEqualTo(new BigDecimal("7.75")); // 3.50 (Latte) + 2.75 (Tea) + 1.50 (Standard Fee)
	}

	@Test
	void testProcessHighPriorityOrder() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setId("req2");
		orderRequest.setName("High Priority Test Order");
		orderRequest.setDescription("Sample high priority order for testing");
		orderRequest.setPriority("HIGH");

		orderRequest.setItems(List.of("Iced Latte", "Green Tea"));

        ResponseEntity<ProcessingResult> response =
                restTemplate.postForEntity("/api/orders/process", orderRequest, ProcessingResult.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ProcessingResult result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.isSuccessful()).isFalse();
	}
}
