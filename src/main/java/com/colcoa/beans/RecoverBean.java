package com.colcoa.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

/**
 * Clase encargada de cambiar la contraseña
 *
 */
@ManagedBean(name = "recoverBean")
public class RecoverBean {
	
	private String username;
	
	private String password;
	
	private String repeatPassword;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@PostConstruct
    public void init() {
		if(FacesContext.getCurrentInstance() != null) {
    		username =  (String)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("RECOVER_USERNAME");
    	}
	}		
	
	public void cambiarContrasena() {
		if(password.equals(repeatPassword)) {
			Usuarios user = manejadorUsuarios.consultarUsuario(username);
			manejadorUsuarios.updatePassword(user, password);
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRepeatPassword() {
		return repeatPassword;
	}
	
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
