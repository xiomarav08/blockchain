package com.colcoa.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "usuariosAdminBean")
public class UsuariosAdminBean implements Serializable{
	
	private static final long serialVersionUID = 1L;


	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	
	private List<Usuarios> listaUsuarios;
	
	
	@PostConstruct
	private void Init() {
		listaUsuarios = manejadorUsuarios.findAll();
	}
	
	public void deleteUser(Usuarios usuario) {
		FacesMessage msg = null;
		if(manejadorUsuarios.deleteUser(usuario)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", "Usuario eliminado con exito");
		}else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar", "El usuario no ha sido eliminado, consulte con un administrador");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<Usuarios> getListaUsuarios() {
		return listaUsuarios;
	}
	
	public void setListaUsuarios(List<Usuarios> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	

}
