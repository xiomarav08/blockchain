package com.colcoa.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.colcoa.enums.EnumTipoTransacion;
import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
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
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	private List<String> imagesSlide ;
	
	@PostConstruct
	private void init() {
		configurarUsuarioInicial();
		imagesSlide = Arrays.asList("banner-bosques.png", "banner-comunidades.png");
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
		        Float balance = Float.parseFloat(prop.getProperty("balance"));
		        
		        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		        Wallet genesisWallet = new Wallet();
		        genesisWallet.generateKeyPair();
		        Transaction genesisTransaction = new Transaction(genesisWallet.getPublicKey(), genesisWallet.getPublicKey(), balance, null);
		        genesisTransaction.setTransactionId("0");
		        byte[] signature = genesisTransaction.generateSignature(genesisWallet.getPrivateKey());
				usuarioAdmin.setPrivateKey(StringUtil.getStringFromKey(genesisWallet.getPublicKey()));
				usuarioAdmin.setPublicKey(StringUtil.getStringFromKey(genesisWallet.getPrivateKey()));
				manejadorUsuarios.almacenarUsuario(usuarioAdmin);
				realizarGenesisTransaction(balance, usuarioAdmin.getUsuario(), genesisTransaction, signature);
			}
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
	private void realizarGenesisTransaction(Float balance, String userAdmin, Transaction genesisTransaction, byte[] signature) {
		TransactionEntity transactionEntity = new TransactionEntity();
		Usuarios usuarioAdmin = manejadorUsuarios.consultarUsuario(userAdmin);
		transactionEntity.setUserSender(usuarioAdmin);
		transactionEntity.setUserRecipient(usuarioAdmin);
		transactionEntity.setSignature(Base64.getEncoder().encodeToString(signature));
		transactionEntity.setTransactionId(genesisTransaction.getTransactionId());
		transactionEntity.setValue(balance);
		transactionEntity.setTipoTransacion(EnumTipoTransacion.RECARGA);
		manejadorTransacciones.almacenarTransaccion(transactionEntity);
	}

	public String getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}
	
	public List<String> getImagesSlide() {
		return imagesSlide;
	}
	
	public void setImagesSlide(List<String> imagesSlide) {
		this.imagesSlide = imagesSlide;
	}
	

}
