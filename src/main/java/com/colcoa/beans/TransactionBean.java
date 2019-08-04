package com.colcoa.beans;

import java.security.PublicKey;
import java.security.Security;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Transaction;

@ManagedBean(name = "transactionBean")
@SessionScoped
public class TransactionBean {
	
	private String publicKey;
	
	private Boolean habilitarSaldo = Boolean.FALSE;
	
	private Boolean habilitarArbol = Boolean.FALSE;
	
	private Usuarios usuario;
	
	private String publicKeyIn;
	
	private Integer numeroArboles;
	
	private final String USUARIO_ADMIN = "admin";
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
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
	
	public void cargarBalance(Integer value) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
			TransactionEntity transactionEntity = new TransactionEntity();
			Usuarios usuarioAdmin = manejadorUsuarios.consultarUsuario(USUARIO_ADMIN);
			PublicKey publicKeyAdmin = StringUtil.getPublicKey(usuarioAdmin.getPublicKey().getBytes());
			PublicKey publicKeyUsuario = StringUtil.getPublicKey(usuarioAdmin.getPublicKey().getBytes());
			Transaction transaction = new Transaction(publicKeyAdmin, publicKeyUsuario, value, null);
		}catch (Exception e) {
			e.printStackTrace();;
		}
	}
	
	public String getPublicKeyIn() {
		return publicKeyIn;
	}
	
	public void setPublicKeyIn(String publicKeyIn) {
		this.publicKeyIn = publicKeyIn;
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
