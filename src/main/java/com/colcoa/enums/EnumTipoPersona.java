package com.colcoa.enums;

public enum EnumTipoPersona {
	
	NATURAL("Natural"),
	JURIDICA("Juridical");
	
	private String message;
	
	private EnumTipoPersona(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
