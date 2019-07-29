package com.colcoa.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Security;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Transaction;
import com.colcocoa.wallet.Wallet;

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

	public String getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}
	
	

}
