package com.colcoa.beans;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.PrimeFaces;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.sun.mail.smtp.SMTPTransport;

@ManagedBean(name = "loginBean")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -2152389656664659476L;

	private String usuario;

	private String clave;

	private Boolean logeado;

	private String recoverEmail;

	private final String USUARIO_ADMIN = "admin";

	private static final String SMTP_SERVER = "plantreforestation.com";
	private static final String USERNAME = "admin@plantreforestation.com";
	private static final String PASSWORD = "*2020Plant";

	private static final String EMAIL_FROM = "admin@plantreforestation.com";

	private static final String EMAIL_SUBJECT = "Test Send Email via SMTP";
	private static final String EMAIL_TEXT = "This email has been self-generated. Please do not reply to this message.\r\n" + 
		"To change your password, follow the link below or copy and paste it into your browser.";

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
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@",
					usuarioBD.getNombre() + " " + usuarioBD.getApellidos());
			if (logeado && usuarioBD.getUsuario().equals(USUARIO_ADMIN)) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("adminMenu/historialContratos.xhtml");
			} else {
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
	 * 
	 * @throws IOException
	 */
	public void logout() throws IOException {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		HttpSession sessionHttp = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		sessionHttp.invalidate();
		logeado = false;
		session.remove("LOGGEDIN_USER");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}

	public void recuperarContrasena() {
		
		
		if (recoverEmail != null && !recoverEmail.equals("")) {
			Usuarios user =  manejadorUsuarios.consultarUsuarioPorEmail(recoverEmail);
			
			// for example, smtp.mailgun.org
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", SMTP_SERVER); // optional, defined in SMTPTransport
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.port", "25"); // default port 25

			Session session = Session.getInstance(prop, null);
			Message msg = new MimeMessage(session);

			try {

				byte[] array = new byte[ 12 ]; // length is bounded by 7
	            new Random().nextBytes( array );
	            String generatedString = new String( array, Charset.forName( "UTF-8" ) );

	            String textEncode = generatedString + System.currentTimeMillis();

	            String encode64 = Base64.encodeBase64String( textEncode.getBytes() );
	            
	            user.setToken(encode64);
	            
	            manejadorUsuarios.updateUser(user);
				
				// from
				msg.setFrom(new InternetAddress(EMAIL_FROM));

				// to
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recoverEmail, false));

				// subject
				msg.setSubject(EMAIL_SUBJECT);

				// content
				msg.setText(EMAIL_TEXT + "http://localhost:8080/services/RecoverPasswordService/recover?token="+user.getToken()+"&email="+user.getEmail());
				msg.setSentDate(new Date());

				// Get SMTPTransport
				SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

				// connect
				t.connect(SMTP_SERVER, USERNAME, PASSWORD);

				// send
				t.sendMessage(msg, msg.getAllRecipients());

				System.out.println("Response: " + t.getLastServerResponse());

				t.close();

			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}
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

	public String getRecoverEmail() {
		return recoverEmail;
	}

	public void setRecoverEmail(String recoverEmail) {
		this.recoverEmail = recoverEmail;
	}
}
