package com.colcoa.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "loginBean")
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = -2152389656664659476L;
	
	private String usuario;
	
	private String clave;
	
	private boolean logeado = false;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	
	/**
	 * Metodo que valida el logueo del usuario
	 * 
	 * @param actionEvent
	 */
	public void login(ActionEvent actionEvent) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		Usuarios usuarioBD = manejadorUsuarios.consultarUsuario(usuario);
		if (usuario != null && clave != null && usuarioBD != null && usuarioBD.getClave().equals(this.clave)) {
			logeado = true;
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", usuarioBD.getNombre());
		} else {
			logeado = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Credenciales no válidas");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("estaLogeado", logeado);
		if (logeado) {
			context.addCallbackParam("view", "billetera.xhtml");
		}
	}

	/**
	 * Metodo para cerrar sesion
	 */
	public void logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		logeado = false;
	}

	public boolean estaLogeado() {
		return logeado;
	}

	public String getNombre() {
		return usuario;
	}

	public void setNombre(String nombre) {
		this.usuario = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
