package com.colcoa.enums;

public enum EnumMedioPago {
	
	BALOTO("Baloto"),
	EFECTY("Efecty"),
	SU_RED("Su Red");
	
	private String message;
	
	private EnumMedioPago(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
