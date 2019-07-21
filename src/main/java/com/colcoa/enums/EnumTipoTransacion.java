package com.colcoa.enums;

public enum EnumTipoTransacion {
	 COMPRA("Compra"),
	 RECARGA("Recarga");
	
	private String message;
	
	private EnumTipoTransacion(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
