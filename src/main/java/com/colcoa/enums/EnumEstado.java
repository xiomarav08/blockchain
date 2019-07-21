package com.colcoa.enums;

public enum EnumEstado {
	
	ACTIVO("activo"),
	INACTIVO("inactivo");
	
	private String message;
	
	private EnumEstado(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
