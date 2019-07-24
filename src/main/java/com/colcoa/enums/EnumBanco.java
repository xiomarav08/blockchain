package com.colcoa.enums;

public enum EnumBanco {
	
	BANCO_AGRARIO("Banco Agrario"),
	BANCO_AV_VILLAS("Banco AV Villas"),
	BANCO_CAJA_SOCIAL("Banco Caja Social"),
	BANCO_COLPATRIA("Banco Colpatria"),
	BANCO_DAVIVIENDA("Banco Davivienda"),
	BANCO_DE_BOGOTA("Banco de Bogota"),
	BANCO_DE_OCCIDENTE("Banco de Occidente"),
	BANCO_GNB_SUDAMERIS("Banco GNB Sudameris"),
	BANCO_PICHINCHA("Banco Pichincha"),
	BANCO_POPULAR("Banco Popular"),
	BANCO_PROCREDIT("Banco Procredit"),
	BANCOLOMBIA("Bancolombia"),
	BANCOOMEVA("Bancoomeva"),
	BBVA_COLOMBIA("BBVA Colombia"),
	CITIBANK("City Bank"),
	ITAÚ("ITAU"),
	BANCO_FALABELLA("Banco Falabella");
	
	private String message;
	
	private EnumBanco(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
