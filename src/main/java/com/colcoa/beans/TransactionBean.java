package com.colcoa.beans;

import java.io.IOException;
import java.security.Security;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import com.colcoa.enums.EnumTipoArbol;
import com.colcoa.enums.EnumTipoTransacion;
import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.UbicacionGeograficaEntity;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
import com.colcocoa.manejadores.ManejadorUbicacionGeografica;
import com.colcocoa.manejadores.ManejadorUsuarios;
import com.colcocoa.wallet.Block;
import com.colcocoa.wallet.Transaction;
import com.colcocoa.wallet.TransactionOutput;
import com.colcocoa.wallet.Wallet;

@ManagedBean(name = "transactionBean")
public class TransactionBean{

	private String publicKey;
	
	private Boolean habilitarSaldo = Boolean.FALSE;
	
	private Boolean habilitarArbol = Boolean.FALSE;
	
	private Usuarios usuario;
	
	private Usuarios usuarioAdmin;
	
	private String publicKeyIn;
	
	private Integer numeroArboles;
	
	private String valorCompra;
	
	private final String USUARIO_ADMIN = "admin";
	
	private String signature;
	
	private Boolean terminosCondiciones;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@Inject
	private UbicacionGeograficaBean ubicacionGeograficaBean;
	
	private List<UbicacionGeograficaEntity> listUbicacionGeografica;
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	@Inject
	private ManejadorUbicacionGeografica manejadorUbicacionGeografica;
	
	@PostConstruct
	public void Init() throws IOException {
		if(FacesContext.getCurrentInstance() != null) {
			recuperarContexto();
		}
	}
	
	public void comprarArbol(Integer numeroArboles, EnumTipoArbol tipoArbol, String valor, String user) {
		usuarioAdmin = manejadorUsuarios.consultarUsuario(USUARIO_ADMIN);
		usuario = manejadorUsuarios.consultarUsuario(user);
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
		transactionEntity.setValueTransaction(valor);
		manejadorTransacciones.almacenarTransaccion(transactionEntity);
		
		signature = transactionEntity.getSignature();
		
		listUbicacionGeografica = manejadorUbicacionGeografica.consultarUbicacion(numeroArboles, tipoArbol);
		
		for(UbicacionGeograficaEntity ubicacionGeograficaEntity: listUbicacionGeografica) {
			ubicacionGeograficaEntity.setOcupado(Boolean.TRUE);
			ubicacionGeograficaEntity.setTransaction(transactionEntity);
			ubicacionGeograficaEntity.setUserPayer(usuario);
			manejadorUbicacionGeografica.actualizarArbol(ubicacionGeograficaEntity);
		}
		
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
	
	public void recuperarContexto() {
		Integer id = manejadorTransacciones.consultarUltimaTransaccion();
		TransactionEntity transactionEntity = manejadorTransacciones.obtenerTransaccionPorId(id);
		this.numeroArboles = transactionEntity.getValue().intValue();
		this.valorCompra = transactionEntity.getValueTransaction();
		this.signature = transactionEntity.getSignature();
		listUbicacionGeografica = manejadorUbicacionGeografica.consultarUbicacionPorTransacion(transactionEntity);
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
	
	public void mostrarUbicacionGeografica() {
		try {
			ubicacionGeograficaBean.inicializarPuntos(listUbicacionGeografica);
			ubicacionGeograficaBean.setListUbicacionGeografica(listUbicacionGeografica);
			FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put("PUNTOS", listUbicacionGeografica);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public String getValorCompra() {
		return valorCompra;
	}
	
	public void setValorCompra(String valorCompra) {
		this.valorCompra = valorCompra;
	}
	
	public Boolean getTerminosCondiciones() {
		return terminosCondiciones;
	}
	
	public void setTerminosCondiciones(Boolean terminosCondiciones) {
		this.terminosCondiciones = terminosCondiciones;
	}
}
