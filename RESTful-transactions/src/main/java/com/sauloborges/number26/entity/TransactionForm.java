package com.sauloborges.number26.entity;

import java.io.Serializable;

/**
 * Represents a class that communicate client / server side
 * @author sauloborges
 *
 */
public class TransactionForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double amount;

	private String type;

	private Long parent_id;

	public TransactionForm() {
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

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parentId) {
		this.parent_id = parentId;
	}

	@Override
	public String toString() {
		return "TransactionForm [amount=" + amount + ", type=" + type + ", parent_id=" + parent_id + "]";
	}
	
	

}
