package ar.com.magm.web.primefaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "registrarUsuarioBean")
public class RegistrarUsuarioBean {
	
	
	private Usuarios usuario;
	
	private String clave;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	public void registrarUsuario(ActionEvent actionEvent) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		if(usuario != null && usuario.getClave().equals(clave)) {
//			WalletBean
//			manejadorUsuarios.almacenarUsuario(usuario);
		}else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden.", "Error");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
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
