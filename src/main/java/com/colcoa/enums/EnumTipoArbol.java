package com.colcoa.enums;

public enum EnumTipoArbol {
	
	
	CACAO("Cacao"),
	FORESTAL("Forestal");

	private String message;
	
	private EnumTipoArbol(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
