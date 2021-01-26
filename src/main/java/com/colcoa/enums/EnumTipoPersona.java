package com.colcoa.enums;

public enum EnumTipoPersona {
	
	NATURAL("Individual"),
	JURIDICA("Organization");
	
	private String message;
	
	private EnumTipoPersona(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
