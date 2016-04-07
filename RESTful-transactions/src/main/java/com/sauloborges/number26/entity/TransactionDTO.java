package com.sauloborges.number26.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long transactionId;

	private Double amount;

	private String type;

	private Long parentId;

	public TransactionDTO() {
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transaction_id) {
		this.transactionId = transaction_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
