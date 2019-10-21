package com.colcoa.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
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
	 * @throws IOException 
	 */
	public void login(ActionEvent actionEvent) throws IOException {
		PrimeFaces context = PrimeFaces.current();
		FacesMessage msg = null;
		Usuarios usuarioBD = manejadorUsuarios.consultarUsuario(usuario);
		if (usuario != null && clave != null && usuarioBD != null && usuarioBD.getClave().equals(this.clave)) {
			logeado = true;
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", usuarioBD.getNombre() +" "+ usuarioBD.getApellidos());
			if (logeado && usuarioBD.getUsuario().equals(USUARIO_ADMIN)) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("adminMenu/historialContratos.xhtml");
			}else {
				FacesContext.getCurrentInstance().getExternalContext().redirect("plantar.xhtml");
			}
			
		} else {
			logeado = false;
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Incorrect username or password");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	    session.put("LOGGEDIN_USER", usuarioBD);
	}

	/**
	 * Metodo para cerrar sesion
	 * @throws IOException 
	 */
	public void logout() throws IOException {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		HttpSession sessionHttp = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		sessionHttp.invalidate();
		logeado = false;
		session.remove("LOGGEDIN_USER");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
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
