package com.colcoa.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Transaction;
import com.colcocoa.wallet.Wallet;
import com.payu.sdk.PayU;
import com.payu.sdk.model.Language;
import com.payu.sdk.utils.LoggerUtil;

@ManagedBean(name = "indexBean")
@SessionScoped
public class IndexBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String nameIndex;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@PostConstruct
	private void init() {
		configurarUsuarioInicial();
		configurarPayU();
	}
	
	private void configurarUsuarioInicial() {
		try {
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("blockchain.properties");
			prop.load(inputStream);
			
			String usuario = prop.getProperty("usuario");			
			nameIndex = prop.getProperty("nameIndex");
			
			Usuarios usuarioBD = manejadorUsuarios.consultarUsuario(usuario);
			 
			if(usuarioBD == null) {   
		        Usuarios usuarioAdmin = new Usuarios();
		        usuarioAdmin.setNombre(prop.getProperty("nombre"));
		        usuarioAdmin.setApellidos(prop.getProperty("apellidos"));
		        usuarioAdmin.setNumeroDeIdentificacion(prop.getProperty("numeroDeIdentificacion"));
		        usuarioAdmin.setTelefono(prop.getProperty("telefono"));
		        usuarioAdmin.setEmail(prop.getProperty("email"));
		        usuarioAdmin.setUsuario(prop.getProperty("usuario"));
		        usuarioAdmin.setClave(prop.getProperty("clave"));
		        usuarioAdmin.setBalance(Float.parseFloat(prop.getProperty("balance")));
		        
		        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		        Wallet genesisWallet = new Wallet();
		        genesisWallet.generateKeyPair();
		        Transaction genesisTransaction = new Transaction(genesisWallet.getPublicKey(), genesisWallet.getPublicKey(), usuarioAdmin.getBalance(), null);
				usuarioAdmin.setPrivateKey(StringUtil.getStringFromKey(genesisWallet.getPublicKey()));
				usuarioAdmin.setPublicKey(StringUtil.getStringFromKey(genesisWallet.getPrivateKey()));
				manejadorUsuarios.almacenarUsuario(usuarioAdmin);
			}
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
	private void configurarPayU() {
		PayU.apiKey = "8KB94yZmS1xu19eF8aKGYG9Nd4"; //Ingresa aquí tu apiKey.
		PayU.apiLogin = "bYC5fH2p5T270G6"; //Ingresa aquí tu apiLogin.
		PayU.language = Language.es; //Ingresa aquí el idioma que prefieras.
		PayU.isTest = true; //Dejarlo verdadero cuando sean pruebas.
		LoggerUtil.setLogLevel(Level.ALL); //Incluirlo únicamente si desea ver toda la traza del log; si solo se desea ver la respuesta, se puede eliminar.
		PayU.paymentsUrl = "https://api.payulatam.com/payments-api/"; //Incluirlo únicamente si desea probar en un servidor de pagos específico, e indicar la ruta del mismo.
		PayU.reportsUrl = "https://api.payulatam.com/reports-api/"; //Incluirlo únicamente si desea probar en un servidor de reportes específico, e indicar la ruta del mismo.
	}

	public String getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}
	
	

}
