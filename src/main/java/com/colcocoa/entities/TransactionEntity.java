package com.colcocoa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.colcoa.enums.EnumTipoTransacion;

@Entity(name = "Transaction")
@Table(name="TRANSACTION")
public class TransactionEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "TRANSACTION_ID_GENERATOR", sequenceName = "SEC_TRANSACTION", allocationSize =1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_ID_GENERATOR")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_SENDER")
	private Usuarios userSender;
	
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_RECIPIENT")
	private Usuarios userRecipient;
	
	@Column(name = "TRANSACTIONID")
	private String transactionId;
	
	@Column(name = "VALUE")
	private Float value;
	
	@Column(name = "SIGNATURE")
	private String signature;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPETRANSACTION")
	private EnumTipoTransacion tipoTransacion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuarios getUserSender() {
		return userSender;
	}

	public void setUserSender(Usuarios userSender) {
		this.userSender = userSender;
	}

	public Usuarios getUserRecipient() {
		return userRecipient;
	}

	public void setUserRecipient(Usuarios userRecipient) {
		this.userRecipient = userRecipient;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionEntity other = (TransactionEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}