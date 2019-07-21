package com.colcoa.dto;

import java.util.Date;

public class SaldoDTO {
	
	private String nombre;
	
	private String nit;
	
	private String saldoTotal;
	
	private Date fechaIngreso;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(String saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
}
