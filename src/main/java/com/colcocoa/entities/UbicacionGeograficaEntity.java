package com.colcocoa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.colcoa.enums.EnumTipoArbol;

@Entity(name = "UbicacionGeografica")
@Table(name="UBICACION_GEOGRAFICA")
public class UbicacionGeograficaEntity extends BaseEntity{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UBICACION_GEO_ID_GENERATOR")
	@GenericGenerator(name = "UBICACION_GEO_ID_GENERATOR", strategy = "native")
	private Long id;
	
	@Column(name = "UBICACION_X")
	private String ubicacionX;
	
	@Column(name = "UBICACION_Y")
	private String ubicacionY;
	
	@Column(name = "OCUPADO")
	private Boolean ocupado;
	
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_PAYER")
	private Usuarios userPayer;
	
	@ManyToOne
	@JoinColumn(name="ID_TRANSACTION")
	private TransactionEntity transaction;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE_TREE")
	private EnumTipoArbol tipoArbol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUbicacionX() {
		return ubicacionX;
	}

	public void setUbicacionX(String ubicacionX) {
		this.ubicacionX = ubicacionX;
	}

	public String getUbicacionY() {
		return ubicacionY;
	}

	public void setUbicacionY(String ubicacionY) {
		this.ubicacionY = ubicacionY;
	}

	public Boolean getOcupado() {
		return ocupado;
	}
	
	public Usuarios getUserPayer() {
		return userPayer;
	}
	
	public void setUserPayer(Usuarios userPayer) {
		this.userPayer = userPayer;
	}
	
	public TransactionEntity getTransaction() {
		return transaction;
	}
	
	public void setTransaction(TransactionEntity transaction) {
		this.transaction = transaction;
	}

	public void setOcupado(Boolean ocupado) {
		this.ocupado = ocupado;
	}
	
	public EnumTipoArbol getTipoArbol() {
		return tipoArbol;
	}
	
	public void setTipoArbol(EnumTipoArbol tipoArbol) {
		this.tipoArbol = tipoArbol;
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
		UbicacionGeograficaEntity other = (UbicacionGeograficaEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
