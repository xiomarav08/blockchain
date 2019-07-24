package com.colcoa.enums;

public enum EnumTipoTarjeta {
	
	VISA("Visa"),
	MASTERCARD("Mastercard");
	
	private String message;
	
	private EnumTipoTarjeta(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
