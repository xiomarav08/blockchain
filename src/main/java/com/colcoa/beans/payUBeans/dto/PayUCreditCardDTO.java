package com.colcoa.beans.payUBeans.dto;

import java.io.Serializable;
import java.util.Date;

public class PayUCreditCardDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String tipoTarjeta;
	
	private String numeroTarjeta;
	
	private Date vencimientoTarjeta;
	
	private String nombreTitular;
	 
	private Double cvc;
	
	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public Date getVencimientoTarjeta() {
		return vencimientoTarjeta;
	}

	public void setVencimientoTarjeta(Date vencimientoTarjeta) {
		this.vencimientoTarjeta = vencimientoTarjeta;
	}

	public String getNombreTitular() {
		return nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public Double getCvc() {
		return cvc;
	}

	public void setCvc(Double cvc) {
		this.cvc = cvc;
	}

	public String getTipoTarjeta() {
		return tipoTarjeta;
	}

	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}
}
