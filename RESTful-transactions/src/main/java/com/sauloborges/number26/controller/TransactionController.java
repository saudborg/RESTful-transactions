package com.sauloborges.number26.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sauloborges.number26.entity.TransactionDTO;
import com.sauloborges.number26.entity.TransactionForm;
import com.sauloborges.number26.exception.ParameterNotNullException;
import com.sauloborges.number26.exception.TransactionNotFoundException;
import com.sauloborges.number26.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(value = "/transactionservice/transaction/{transaction_id}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE })
	public String putTransaction(@PathVariable("transaction_id") Long transaction_id,
			@ModelAttribute TransactionForm transactionForm) {

		JsonObject response = new JsonObject();
		try {
			TransactionDTO transaction = validateTransaction(transactionForm, transaction_id);
			transactionService.save(transaction);
			response.addProperty("status", "ok");
		} catch (Exception e) {
			response.addProperty("status", "error");
			response.addProperty("message", e.getMessage());
		}
		return response.toString();
	}

	private TransactionDTO validateTransaction(TransactionForm transactionForm, Long transaction_id)
			throws ParameterNotNullException, TransactionNotFoundException {
		if (transactionForm.getAmount() == null) {
			throw new ParameterNotNullException("amount");
		}
		if (transactionForm.getType() == null) {
			throw new ParameterNotNullException("type");
		}
		validateParentId(transactionForm.getParent_id());
		
		TransactionDTO transaction = new TransactionDTO();
		transaction.setTransactionId(transaction_id);
		transaction.setAmount(transactionForm.getAmount());
		transaction.setType(transactionForm.getType());
		transaction.setParentId(transactionForm.getParent_id());
		
		return transaction;
	}

	private void validateParentId(Long parentId) throws TransactionNotFoundException {
		if (parentId != null) {
			TransactionDTO transactionId = transactionService.findByTransactionId(parentId);
			if (transactionId == null) {
				throw new TransactionNotFoundException(parentId);
			}
		}
	}

	@RequestMapping(value = "/transactionservice/transaction/{transaction_id}", method = RequestMethod.GET)
	public String getTransaction(@PathVariable("transaction_id") Long transaction_id) {
		Gson gson = new Gson();

		JsonObject response = new JsonObject();
		if (transaction_id == null) {
			response.addProperty("status", "error");
			response.addProperty("message", "transaction_id cannot be blank");
			return response.toString();
		}

		TransactionDTO transactionDTO = transactionService.findByTransactionId(transaction_id);
		if (transactionDTO == null) {
			response.addProperty("status", "error");
			response.addProperty("message", "Entity not found for transaction_id: " + transaction_id);
			return response.toString();
		}
		
		TransactionForm form = transformDTOinForm(transactionDTO);
		return gson.toJson(form);
	}

	private TransactionForm transformDTOinForm(TransactionDTO transactionDTO) {
		TransactionForm form = new TransactionForm();
		form.setAmount(transactionDTO.getAmount());
		form.setParent_id(transactionDTO.getParentId());
		form.setType(transactionDTO.getType());
		return form;
	}

	@RequestMapping(value = "/transactionservice/types/{type}", method = RequestMethod.GET)
	public String getListTransactionByType(@PathVariable("type") String type) {
		JsonObject response = new JsonObject();

		if (type == null) {
			response.addProperty("status", "error");
			response.addProperty("message", "type cannot be null");
			return response.toString();
		}

		List<Long> transactionList = transactionService.findByTransactionByType(type);
		if (transactionList.isEmpty()) {
			response.addProperty("status", "error");
			response.addProperty("message", "type cannot be found");
			return response.toString();
		}
		Gson gson = new Gson();
		return gson.toJson(transactionList);
	}

	@RequestMapping(value = "/transactionservice/sum/{transaction_id}", method = RequestMethod.GET)
	public String getSumTransactionByParentId(@PathVariable("transaction_id") Long parentId) {
		JsonObject response = new JsonObject();

		if (parentId == null) {
			response.addProperty("status", "error");
			response.addProperty("message", "type cannot be null");
			return response.toString();
		}
		
		try {
			validateParentId(parentId);
		} catch (TransactionNotFoundException e) {
			response.addProperty("status", "error");
			response.addProperty("message", e.getMessage());
			return response.toString();
		}

		Double sum = transactionService.findByTransactionByType(parentId);
		response.addProperty("sum", sum);
		return response.toString();
	}
}
