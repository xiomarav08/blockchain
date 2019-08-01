package com.colcoa.beans;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "loginBean")
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = -2152389656664659476L;
	
	private String usuario;
	
	private String clave;
	
	private Boolean logeado;
	
	private final String USUARIO_ADMIN = "admin";
	
	@PostConstruct
	private void Init() {
		logeado = Boolean.FALSE;
	}
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	
	/**
	 * Metodo que valida el logueo del usuario
	 * 
	 * @param actionEvent
	 */
	public void login(ActionEvent actionEvent) {
		PrimeFaces context = PrimeFaces.current();
		FacesMessage msg = null;
		Usuarios usuarioBD = manejadorUsuarios.consultarUsuario(usuario);
		if (usuario != null && clave != null && usuarioBD != null && usuarioBD.getClave().equals(this.clave)) {
			logeado = true;
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", usuarioBD.getNombre() +" "+ usuarioBD.getApellidos());
		} else {
			logeado = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Credenciales no válidas");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	    session.put("LOGGEDIN_USER", usuarioBD);
		
		if (logeado && usuarioBD.getUsuario().equals(USUARIO_ADMIN)) {
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "adminMenu/historialContratos.xhtml");
		}else {
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "plantar.xhtml");
		}
	}

	/**
	 * Metodo para cerrar sesion
	 */
	public void logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		logeado = false;
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
	}
	
	public boolean estaLogeado() {
		return logeado;
	}
	
	public Boolean getLogeado() {
		return logeado;
	}

	public void setLogeado(Boolean logeado) {
		this.logeado = logeado;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
}
