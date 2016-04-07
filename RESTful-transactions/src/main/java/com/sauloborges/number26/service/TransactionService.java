package com.sauloborges.number26.service;

import java.util.List;

import com.sauloborges.number26.entity.TransactionDTO;

public interface TransactionService {
	
	public void save(TransactionDTO transactionDTO);

	public TransactionDTO findByTransactionId(Long transaction_id);

	public List<Long> findByTransactionByType(String type);

	public Double findByTransactionByType(Long parentId);

}
