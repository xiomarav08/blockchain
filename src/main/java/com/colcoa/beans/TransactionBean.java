package com.colcoa.beans;

import java.io.IOException;
import java.security.Security;
import java.util.Base64;
import java.util.List;
import java.util.spi.TimeZoneNameProvider;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import com.colcoa.enums.EnumTipoTransacion;
import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.Block;
import com.colcocoa.wallet.Transaction;
import com.colcocoa.wallet.TransactionOutput;
import com.colcocoa.wallet.Wallet;

@ManagedBean(name = "transactionBean")
@SessionScoped
public class TransactionBean {
	
	private String publicKey;
	
	private Boolean habilitarSaldo = Boolean.FALSE;
	
	private Boolean habilitarArbol = Boolean.FALSE;
	
	private Usuarios usuario;
	
	private Usuarios usuarioAdmin;
	
	private String publicKeyIn;
	
	private Integer numeroArboles;
	
	private final String USUARIO_ADMIN = "admin";
	
	private String signature;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	@PostConstruct
	public void Init() throws IOException {
		FacesMessage msg = null;
		usuario = (Usuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGEDIN_USER");
		usuarioAdmin = manejadorUsuarios.consultarUsuario(USUARIO_ADMIN);
		if(usuario == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El usuario no se a autenticado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
		}
	}
	
	public void comprarArbol() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		TransactionEntity transactionEntity = new TransactionEntity();
		
		Wallet walletAdmin = new Wallet();
		walletAdmin.generateKeyPair();
		
		Wallet walletUser = new Wallet();
		walletUser.generateKeyPair();
		
		Wallet coinbase = new Wallet();
		coinbase.generateKeyPair();
		
		Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletUser.getPublicKey(), numeroArboles, null);
		genesisTransaction.setTransactionId("0"); //manually set the transaction id
		genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getReciepient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
		WalletBean.UTXOs.put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.
		
		Block blockGenesis = new Block("0");
		Transaction transaction = walletUser.sendFunds(walletAdmin.getPublicKey(), numeroArboles);
		WalletBean.addBlock(blockGenesis);
		
		Block blockTransaction = new Block(blockGenesis.hash);
		blockTransaction.addTransaction(transaction);
		WalletBean.addBlock(blockTransaction);
		
		transactionEntity.setTipoTransacion(EnumTipoTransacion.COMPRA);
		transactionEntity.setSignature(Base64.getEncoder().encodeToString(transaction.getSignature()));
		transactionEntity.setUserSender(usuario);
		transactionEntity.setUserRecipient(usuarioAdmin);
		transactionEntity.setTransactionId(transaction.getTransactionId());
		transactionEntity.setValue(Float.valueOf(numeroArboles));
		manejadorTransacciones.almacenarTransaccion(transactionEntity);
		signature = transactionEntity.getSignature();
	}
	
	public void cargarBalance(Integer value) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
			TransactionEntity transactionEntity = new TransactionEntity();
			
			Wallet walletAdmin = new Wallet();
			walletAdmin.generateKeyPair();
			
			Wallet walletUser = new Wallet();
			walletUser.generateKeyPair();
			
			Wallet coinbase = new Wallet();
			coinbase.generateKeyPair();
			
			Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletAdmin.getPublicKey(), value, null);
			genesisTransaction.setTransactionId("0"); //manually set the transaction id
			genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getReciepient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
			WalletBean.UTXOs.put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.
			
			Block blockGenesis = new Block("0");
			Transaction transaction = walletAdmin.sendFunds(walletUser.getPublicKey(), value);
			WalletBean.addBlock(blockGenesis);
			
			Block blockTransaction = new Block(blockGenesis.hash);
			blockTransaction.addTransaction(transaction);
			WalletBean.addBlock(blockTransaction);
			
			transactionEntity.setTipoTransacion(EnumTipoTransacion.RECARGA);
			transactionEntity.setSignature(Base64.getEncoder().encodeToString(transaction.getSignature()));
			transactionEntity.setUserSender(usuarioAdmin);
			transactionEntity.setUserRecipient(usuario);
			transactionEntity.setTransactionId(transaction.getTransactionId());
			transactionEntity.setValue(Float.valueOf(value));
			manejadorTransacciones.almacenarTransaccion(transactionEntity);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer calcularSaldo() {
		List<TransactionEntity> listadoTransaciones = manejadorTransacciones.consultarTransaccionesPorUsuario(usuario);
		Float saldoTotal = 0f;
		for (TransactionEntity transactionEntity : listadoTransaciones) {
			if(EnumTipoTransacion.RECARGA.equals(transactionEntity.getTipoTransacion())) {
				saldoTotal = saldoTotal + transactionEntity.getValue();
			}else if(EnumTipoTransacion.COMPRA.equals(transactionEntity.getTipoTransacion())){
				saldoTotal = saldoTotal - transactionEntity.getValue();
			}
		}
		return Math.round(saldoTotal);
	}
	
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getPublicKeyIn() {
		return publicKeyIn;
	}
	
	public void setPublicKeyIn(String publicKeyIn) {
		this.publicKeyIn = publicKeyIn;
	}
	
	public void verSaldo(ActionEvent actionEvent) {
		habilitarArbol = Boolean.FALSE;
		habilitarSaldo = Boolean.TRUE;
	}
	
	public void verArbol(ActionEvent actionEvent) {
		habilitarArbol = Boolean.TRUE;
		habilitarSaldo = Boolean.FALSE;
	}
	
	public Integer getNumeroArboles() {
		return numeroArboles;
	}
	
	public void setNumeroArboles(Integer numeroArboles) {
		this.numeroArboles = numeroArboles;
	}
	
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public Boolean getHabilitarSaldo() {
		return habilitarSaldo;
	}

	public void setHabilitarSaldo(Boolean habilitarSaldo) {
		this.habilitarSaldo = habilitarSaldo;
	}

	public Boolean getHabilitarArbol() {
		return habilitarArbol;
	}

	public void setHabilitarArbol(Boolean habilitarArbol) {
		this.habilitarArbol = habilitarArbol;
	}
	
	public Usuarios getUsuarioAdmin() {
		return usuarioAdmin;
	}
	
	public void setUsuarioAdmin(Usuarios usuarioAdmin) {
		this.usuarioAdmin = usuarioAdmin;
	}
	
	public Usuarios getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
}
