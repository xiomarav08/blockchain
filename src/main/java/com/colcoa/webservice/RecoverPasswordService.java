package com.colcoa.webservice;

import java.net.URI;
import java.net.URISyntaxException;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;

@Path("/RecoverPasswordService")
@Consumes({ "application/json" })
public class RecoverPasswordService {
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@GET
	@Path("/recover")
	public Response recoverPassword(@QueryParam("token") String token, @QueryParam("email") String email) {
		Usuarios user = manejadorUsuarios.consultarUsuarioPorEmail(email);
		try {
			if(token.equals(user.getToken())) {
				URI url = new URI("https://localhost:8080/recuperarContrasena.xhtml");
				return Response.temporaryRedirect(url).build();
			}
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}
}
