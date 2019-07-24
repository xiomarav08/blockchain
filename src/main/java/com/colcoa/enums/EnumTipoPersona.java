package com.colcoa.enums;

public enum EnumTipoPersona {
	
	NATURAL("Natural"),
	JURIDICA("Juridica");
	
	private String message;
	
	private EnumTipoPersona(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
