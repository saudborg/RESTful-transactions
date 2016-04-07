package com.sauloborges.number26;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.service.TransactionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@WebAppConfiguration
public class TransactionServiceTest {

	@Autowired
	private TransactionService transactionService;

	@Test
	public void saveTransaction() {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionId(1l);
		dto.setAmount(100D);
		dto.setType("type1");
		transactionService.save(dto);

		TransactionDTO transactionDatabase = transactionService.findByTransactionId(1L);
		assertNotNull(transactionDatabase);

		assertEquals(dto.getTransactionId(), transactionDatabase.getTransactionId());
		assertEquals(dto.getAmount(), transactionDatabase.getAmount());
		assertEquals(dto.getType(), transactionDatabase.getType());
	}

}
