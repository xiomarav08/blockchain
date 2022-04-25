package com.colcoa.enums;

public enum EnumEspecies {
	
	
	TODAS("Todas"),
	CEDRO("Cedro"),
	MELINA("Melina"),
	EUCALIPTO_GRANDIS("Eucalipto Grandis"),
	GUAYACAN_ROSADO("Guayacán Rosado");
	
	private String message;
	
	private EnumEspecies(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
