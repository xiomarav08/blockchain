package com.colcoa.beans.payUBeans.dto;

import java.io.Serializable;

import com.colcoa.enums.EnumBanco;
import com.colcoa.enums.EnumTipoPersona;

public class PayUPSEDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private EnumBanco Banco;
	
	private String nombreTitular;
	
	private EnumTipoPersona tipocliente;
	
	private Integer numeroIdentificacion;
	
	private String indicativo;
	
	private Integer Telefono;

	public EnumBanco getBanco() {
		return Banco;
	}

	public void setBanco(EnumBanco banco) {
		Banco = banco;
	}

	public String getNombreTitular() {
		return nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public EnumTipoPersona getTipocliente() {
		return tipocliente;
	}

	public void setTipocliente(EnumTipoPersona tipocliente) {
		this.tipocliente = tipocliente;
	}

	public Integer getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(Integer numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getIndicativo() {
		return indicativo;
	}

	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public Integer getTelefono() {
		return Telefono;
	}

	public void setTelefono(Integer telefono) {
		Telefono = telefono;
	}
}
