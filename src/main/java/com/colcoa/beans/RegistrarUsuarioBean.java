package com.colcoa.beans;

import java.io.IOException;
import java.io.Serializable;
import java.security.Security;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import com.colcoa.enums.EnumTipoDocumento;
import com.colcoa.enums.EnumTipoPersona;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Wallet;

@ManagedBean(name = "registrarUsuarioBean")
public class RegistrarUsuarioBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Usuarios usuario;
	
	private String clave;
	
	private Boolean tratamientoDeDatos;
	
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
				
				PrimeFaces.current().ajax().addCallbackParam("view", "login.xhtml");
			    
			}else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", "Error");
			}
		}else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User already exists", "Error");	
		}
		PrimeFaces.current().dialog().showMessageDynamic(msg);
	}
	
	private Usuarios generarClavesUsuario(Usuarios usuarios) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		Wallet wallet = new Wallet();
		wallet.generateKeyPair();
		usuarios.setPublicKey(StringUtil.getStringFromKey(wallet.getPublicKey()));
		usuarios.setPrivateKey(StringUtil.getStringFromKey(wallet.getPrivateKey()));
		return usuarios;
	}
	
	public void abrirTratamientoDatos() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("politica.xhtml");
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
	
	public EnumTipoPersona[] getTiposPersona() {
		return EnumTipoPersona.values();
	}
	
	public EnumTipoDocumento[] getTiposDocumento() {
		return EnumTipoDocumento.values();
	}
	
	public Boolean getTratamientoDeDatos() {
		return tratamientoDeDatos;
	}
	
	public void setTratamientoDeDatos(Boolean tratamientoDeDatos) {
		this.tratamientoDeDatos = tratamientoDeDatos;
	}
}
