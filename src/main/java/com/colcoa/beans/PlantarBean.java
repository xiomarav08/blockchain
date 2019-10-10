package com.colcoa.beans;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "plantarBean")
public class PlantarBean {
	
	private List<String> arboles;
	
	@PostConstruct
	public void Init() {
		arboles = Arrays.asList("Abarco", "Caoba", "Moncoro", "	Cacao");
	}

	
	/**
	 * @param arbol
	 */
	private void comprar(String arbol) {
		
	}
	
	public List<String> getArboles() {
		return arboles;
	}
	
	public void setArboles(List<String> arboles) {
		this.arboles = arboles;
	}
}
