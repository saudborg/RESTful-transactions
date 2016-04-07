package com.sauloborges.number26;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.repository.TransactionRepository;
import com.sauloborges.number26.utils.TransactionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@WebAppConfiguration
public class TransactionControllerGetTest {

	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private TransactionTestUtils utils;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		repository.deleteAll();
	}

	@Test
	public void simpleGetOK() throws Exception {
		TransactionDTO transaction = utils.createTransaction();

		mockMvc.perform(get("/transactionservice/transaction/1")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("amount", is(transaction.getAmount())))//
				.andExpect(jsonPath("type", is(transaction.getType())));
	}

	@Test
	public void getWithoutTransactionId() throws Exception {
		utils.createTransaction();

		mockMvc.perform(get("/transactionservice/transaction/")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(status().isNotFound());
	}

	@Test
	public void getWithTransactionIdInvalid() throws Exception {
		mockMvc.perform(get("/transactionservice/transaction/5")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("status", is("error")))//
				.andExpect(jsonPath("message", is("Entity not found for transaction_id: 5")));
	}

}
