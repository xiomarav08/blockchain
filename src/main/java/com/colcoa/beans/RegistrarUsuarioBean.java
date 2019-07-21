package com.colcoa.beans;

import java.io.Serializable;
import java.security.Security;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Wallet;

@ManagedBean(name = "registrarUsuarioBean")
public class RegistrarUsuarioBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Usuarios usuario;
	
	private String clave;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@PostConstruct
	public void Init() {
		usuario = new Usuarios();
	}
	
	public void registrarUsuario(ActionEvent actionEvent) {
		FacesMessage msg = null;
		Usuarios usuarioBD = manejadorUsuarios.consultarUsuario(usuario.getUsuario());
		if(usuarioBD == null) {
			if(usuario != null && usuario.getClave().equals(clave)) {
				usuario = generarClavesUsuario(usuario);
				manejadorUsuarios.almacenarUsuario(usuario);
			}else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden.", "Error");
			}
		}else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario ya existe", "Error");	
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private Usuarios generarClavesUsuario(Usuarios usuarios) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		Wallet wallet = new Wallet();
		wallet.generateKeyPair();
		usuarios.setPublicKey(StringUtil.getStringFromKey(wallet.getPublicKey()));
		usuarios.setPrivateKey(StringUtil.getStringFromKey(wallet.getPrivateKey()));
		return usuarios;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	
	
	
	

}
