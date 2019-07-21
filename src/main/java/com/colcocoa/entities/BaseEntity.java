package com.colcocoa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.colcoa.enums.EnumEstado;

@MappedSuperclass
public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="ESTADO")
	@Enumerated(EnumType.STRING)
	private EnumEstado estado; 
	
	public EnumEstado getEstado() {
		return estado;
	}
	
	public void setEstado(EnumEstado estado) {
		this.estado = estado;
	}
}
