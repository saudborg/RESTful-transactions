package com.sauloborges.number26.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.service.TransactionService;

@Service
public class TransactionTestUtils {
	
	@Autowired
	private TransactionService transactionService;
	
	public TransactionDTO createTransaction() {
		return createTransaction(1l, 100d, "type1");
	}

	public TransactionDTO createTransaction(Long id, Double amount, String type) {
		return createTransaction(id, amount, type, null);
	}

	public TransactionDTO createTransaction(Long id, Double amount, String type, Long parentId) {
		TransactionDTO dto = new TransactionDTO();
		dto.setTransactionId(id);
		dto.setAmount(amount);
		dto.setType(type);
		dto.setParentId(parentId);
		transactionService.save(dto);
		return dto;
	}

}
