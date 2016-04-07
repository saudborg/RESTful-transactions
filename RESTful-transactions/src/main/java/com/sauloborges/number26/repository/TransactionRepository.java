package com.sauloborges.number26.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sauloborges.number26.entity.TransactionDTO;

public interface TransactionRepository extends CrudRepository<TransactionDTO, Long> {
	
	List<TransactionDTO> findByType(String type);
	
	List<TransactionDTO> findByParentId(Long parentId);

}
