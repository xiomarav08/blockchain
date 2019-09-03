package com.colcoa.enums;

public enum EnumTipoDocumento {
	
	CEDULA_DE_CIUDADANIA("C�dula de ciudadania", "CC"),
	CEDULA_DE_EXTRANJERIA("C�dula de extranjer�a", "CE"),
	PASAPORTE("PASAPORTE", "TP"),
	NIT("Nit", "NIT");
	
	private String message;
	
	private String value;
	
	private EnumTipoDocumento(String message, String value) {
		this.message = message;
		this.value = value;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getValue() {
		return value;
	}
}
