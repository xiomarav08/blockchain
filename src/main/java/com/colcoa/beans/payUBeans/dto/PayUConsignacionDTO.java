package com.colcoa.beans.payUBeans.dto;

import java.io.Serializable;

import com.colcoa.enums.EnumMedioPago;

public class PayUConsignacionDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private EnumMedioPago medioPago;
	
	private Integer numeroConsignacion;

	public EnumMedioPago getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(EnumMedioPago medioPago) {
		this.medioPago = medioPago;
	}

	public Integer getNumeroConsignacion() {
		return numeroConsignacion;
	}

	public void setNumeroConsignacion(Integer numeroConsignacion) {
		this.numeroConsignacion = numeroConsignacion;
	}
	
	

}
