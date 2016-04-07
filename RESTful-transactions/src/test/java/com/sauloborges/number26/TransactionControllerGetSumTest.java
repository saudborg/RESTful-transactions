package com.sauloborges.number26;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
public class TransactionControllerGetSumTest {

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
	public void simpleGetSumOK() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction();
		Double sum = transaction1.getAmount();

		mockMvc.perform(get("/transactionservice/sum/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("sum", is(sum)))//
				;
	}

	@Test
	public void getSum2() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction();
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1", 1l);
		Double sum = transaction1.getAmount() + transaction2.getAmount();

		mockMvc.perform(get("/transactionservice/sum/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("sum", is(sum)))//
				;
	}

	@Test
	public void getSum3() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction();
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1", 1l);
		TransactionDTO transaction3 = utils.createTransaction(3l, 300d, "type1", 1l);

		Double sum = transaction1.getAmount() + transaction2.getAmount() + transaction3.getAmount();

		mockMvc.perform(get("/transactionservice/sum/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("sum", is(sum)))//
				;
	}

	@Test
	public void getSum3And2WithDifferentsParents() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction();
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1", 1l);
		TransactionDTO transaction3 = utils.createTransaction(3l, 300d, "type1", 1l);

		utils.createTransaction(4l, 400d, "type1"); // transaction 4
		utils.createTransaction(5l, 500d, "type1"); // transaction 5
		utils.createTransaction(6l, 600d, "type1"); // transaction 6

		Double sum = transaction1.getAmount() + transaction2.getAmount() + transaction3.getAmount();

		mockMvc.perform(get("/transactionservice/sum/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("sum", is(sum)))//
				;
	}

	@Test
	public void getSum0() throws Exception {
		utils.createTransaction();
		utils.createTransaction(2l, 200d, "type1", 1l);
		utils.createTransaction(3l, 300d, "type1", 1l);

		mockMvc.perform(get("/transactionservice/sum/10")// this transaction
															// does't exists
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("status", is("error")))//
				.andExpect(jsonPath("message", is("This transaction cannot be parent, it does't exists: 10")))//
				;
	}
}
