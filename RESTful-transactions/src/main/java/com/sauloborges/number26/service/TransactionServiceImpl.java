package com.sauloborges.number26.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public void save(TransactionDTO transactionDTO) {
		transactionRepository.save(transactionDTO);
	}

	@Override
	public TransactionDTO findByTransactionId(Long transaction_id) {
		TransactionDTO transactionEntity = transactionRepository.findOne(transaction_id);
		if (transactionEntity == null)
			return null;
		return transactionEntity;
	}

	@Override
	public List<Long> findByTransactionByType(String type) {
		List<TransactionDTO> findByType = transactionRepository.findByType(type);
		List<Long> listIds = new ArrayList<Long>();
		
		for (TransactionDTO transactionEntity : findByType) {
			listIds.add(transactionEntity.getTransactionId());
		}
		return listIds;
	}

	@Override
	public Double findByTransactionByType(Long parentId) {
		TransactionDTO transactionEntity = transactionRepository.findOne(parentId);
		List<TransactionDTO> findByParentId = transactionRepository.findByParentId(parentId);

		Double sum = transactionEntity.getAmount();
		for (TransactionDTO transactionEntity2 : findByParentId) {
			sum = sum + transactionEntity2.getAmount();
		}
		return sum;
	}

}
