package com.sauloborges.number26;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.repository.TransactionRepository;
import com.sauloborges.number26.utils.TransactionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@WebAppConfiguration
public class TransactionControllerGetListTest {

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
	public void simpleGetListOK() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction();
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1");

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(transaction1.getTransactionId().intValue());
		expected.add(transaction2.getTransactionId().intValue());
		ResultActions andExpect = mockMvc
				.perform(get("/transactionservice/types/type1")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("$", is(expected)))//
				;
		System.out.println(andExpect);
	}

	@Test
	public void simpleGetListWithOneDiferentOK() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction(); // default
																	// type1
		utils.createTransaction(2l, 200d, "type2"); // transaction2
		TransactionDTO transaction3 = utils.createTransaction(3l, 200d, "type1");

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(transaction1.getTransactionId().intValue());
		expected.add(transaction3.getTransactionId().intValue());
		ResultActions andExpect = mockMvc
				.perform(get("/transactionservice/types/type1")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("$", is(expected)))//
				;
		System.out.println(andExpect);
	}

	@Test
	public void getListWithoutType() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction(); // default
																	// type1
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1");

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(transaction1.getTransactionId().intValue());
		expected.add(transaction2.getTransactionId().intValue());
		ResultActions andExpect = mockMvc
				.perform(get("/transactionservice/types/")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(status().isNotFound())//
				;
		System.out.println(andExpect);
	}

	@Test
	public void getListEmpty() throws Exception {
		TransactionDTO transaction1 = utils.createTransaction(); // default
																	// type1
		TransactionDTO transaction2 = utils.createTransaction(2l, 200d, "type1");

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(transaction1.getTransactionId().intValue());
		expected.add(transaction2.getTransactionId().intValue());
		ResultActions andExpect = mockMvc
				.perform(get("/transactionservice/types/typeX")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))//
				.andExpect(jsonPath("status", is("error"))).andExpect(jsonPath("message", is("type cannot be found")));
		System.out.println(andExpect);
	}

}
