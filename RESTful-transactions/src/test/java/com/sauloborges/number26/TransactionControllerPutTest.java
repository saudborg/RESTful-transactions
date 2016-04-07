package com.sauloborges.number26;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.sauloborges.number26.service.TransactionService;
import com.sauloborges.number26.utils.TransactionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@WebAppConfiguration
public class TransactionControllerPutTest {

	@Autowired
	private TransactionTestUtils utils;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testSimplePut() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "100")//
				.param("type", "type1")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(status().isOk());

		TransactionDTO transactionDatabase = transactionService.findByTransactionId(1L);
		assertNotNull(transactionDatabase);

		assertEquals(new Long(1), transactionDatabase.getTransactionId());
		assertEquals(new Double(100d), transactionDatabase.getAmount());
		assertEquals("type1", transactionDatabase.getType());
	}

	@Test
	public void testPutWithoutAmount() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("type", "type1")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(jsonPath("status", is("error")))//
				.andExpect(jsonPath("message", is("Parameter cannot be null: amount")));

	}

	@Test
	public void testPutInvalidAmount() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "cannot be text, must be double")//
				.param("type", "type1")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(status().isBadRequest());
		// TODO
	}

	@Test
	public void testPutWithoutType() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "100")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(jsonPath("status", is("error")))//
				.andExpect(jsonPath("message", is("Parameter cannot be null: type")));

	}

	@Test
	public void testPutDifferentCaractersType() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "100")//
				.param("type", "type βéå")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(status().isOk());
	}

	@Test
	public void testPutParentId() throws Exception {
		// Create a transaction with id = 1
		utils.createTransaction();

		mockMvc.perform(put("/transactionservice/transaction/2")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "100")//
				.param("type", "type1")//
				.param("parent_id", "1")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(status().isOk());

		TransactionDTO transactionDatabase = transactionService.findByTransactionId(2L);
		assertNotNull(transactionDatabase);

		assertEquals(new Long(2), transactionDatabase.getTransactionId());
		assertEquals(new Double(100d), transactionDatabase.getAmount());
		assertEquals("type1", transactionDatabase.getType());
		assertEquals(new Long(1), transactionDatabase.getParentId());
	}

	@Test
	public void testPutParentIdInvalid() throws Exception {
		mockMvc.perform(put("/transactionservice/transaction/2")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "100")//
				.param("type", "type1")//
				.param("parent_id", "5")// This ID (1) not exists
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(jsonPath("status", is("error")))//
				.andExpect(jsonPath("message", is("This transaction cannot be parent, it does't exists: 5")));
	}

	@Test
	public void testPutAlreadyExists() throws Exception {
		TransactionDTO transaction = utils.createTransaction();

		TransactionDTO transactionDatabase = transactionService.findByTransactionId(transaction.getTransactionId());
		assertNotNull(transactionDatabase);

		// PUT in the same transaction changing amount and type
		mockMvc.perform(put("/transactionservice/transaction/1")//
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))//
				.header("Content-Type", "application/json")//
				.param("amount", "200")//
				.param("type", "type2")//
				.sessionAttr("transactionDTO", new TransactionDTO())//
		).andExpect(status().isOk());

		TransactionDTO transactionChangedDatabase = transactionService.findByTransactionId(1L);
		assertNotNull(transactionChangedDatabase);

		assertEquals(new Long(1), transactionChangedDatabase.getTransactionId());
		assertEquals(new Double(200d), transactionChangedDatabase.getAmount());
		assertEquals("type2", transactionChangedDatabase.getType());

		assertFalse(transactionDatabase.getAmount() == transactionChangedDatabase.getAmount());
		assertFalse(transactionDatabase.getType() == transactionChangedDatabase.getType());
		assertEquals(transactionDatabase.getTransactionId(), transactionChangedDatabase.getTransactionId());

	}

}
