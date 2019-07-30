package com.colcoa.beans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.colcocoa.entities.Usuarios;

@ManagedBean(name = "transactionBean")
@SessionScoped
public class TransactionBean {
	
	private String publicKey;
	
	private Boolean habilitarSaldo = Boolean.FALSE;
	
	private Boolean habilitarArbol = Boolean.FALSE;
	
	private Usuarios usuario;
	
	private Integer numeroArboles;
	
	@PostConstruct
	public void Init() {
		FacesMessage msg = null;
		usuario = (Usuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGEDIN_USER");
		if(usuario == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no se a autenticado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
		}
	}
	
	public void comprarArbol() {
		
	}
	
	public void verSaldo(ActionEvent actionEvent) {
		habilitarArbol = Boolean.FALSE;
		habilitarSaldo = Boolean.TRUE;
	}
	
	public void verArbol(ActionEvent actionEvent) {
		habilitarArbol = Boolean.TRUE;
		habilitarSaldo = Boolean.FALSE;
	}
	
	public Integer getNumeroArboles() {
		return numeroArboles;
	}
	
	public void setNumeroArboles(Integer numeroArboles) {
		this.numeroArboles = numeroArboles;
	}
	
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public Boolean getHabilitarSaldo() {
		return habilitarSaldo;
	}

	public void setHabilitarSaldo(Boolean habilitarSaldo) {
		this.habilitarSaldo = habilitarSaldo;
	}

	public Boolean getHabilitarArbol() {
		return habilitarArbol;
	}

	public void setHabilitarArbol(Boolean habilitarArbol) {
		this.habilitarArbol = habilitarArbol;
	}
	
	public Usuarios getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
}
