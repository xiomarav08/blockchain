package ar.com.magm.web.primefaces;

import java.io.Serializable;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Init;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import com.colcocoa.wallet.Block;
import com.colcocoa.wallet.StringUtil;
import com.colcocoa.wallet.Transaction;
import com.colcocoa.wallet.TransactionOutput;
import com.colcocoa.wallet.Wallet;

@ManagedBean(name = "WalletBean")
@SessionScoped
public class WalletBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Boolean habilitarLlave = Boolean.FALSE;
	
	private Boolean habilitarSaldo = Boolean.FALSE;
	
	private Boolean habilitarArbol = Boolean.FALSE;
	
	private Integer numeroArboles = 0;
	
	private String publicKey;
	
	private String publicKeyIn;
	
	private String privateKey;
	
	private Wallet billeteraUsuario;
	
	private Wallet billeteraAdmin;
	
	private String saldo;
	
	private String saldoAdmin;
	
	private Block genesis;
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static Transaction genesisTransaction;
	
	@Init
    public void init() {
		
    }
	
	public void verBilletera(ActionEvent actionEvent) {
		this.generateWallet();
		habilitarLlave = Boolean.TRUE;
		habilitarArbol = Boolean.FALSE;
		habilitarSaldo = Boolean.FALSE;
		
	}
	
	public void verSaldo(ActionEvent actionEvent) {
		habilitarLlave = Boolean.FALSE;
		habilitarArbol = Boolean.FALSE;
		habilitarSaldo = Boolean.TRUE;
	}
	
	public void verArbol(ActionEvent actionEvent) {
		habilitarLlave = Boolean.FALSE;
		habilitarArbol = Boolean.TRUE;
		habilitarSaldo = Boolean.FALSE;
	}
	
	public void generateWallet() {		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		this.billeteraUsuario = new Wallet();
		this.billeteraAdmin = new Wallet();
		
		this.billeteraUsuario.generateKeyPair();
		this.publicKey = StringUtil.getStringFromKey(billeteraUsuario.publicKey);
		this.privateKey = StringUtil.getStringFromKey(billeteraUsuario.privateKey);
		
		Wallet coinbase = new Wallet();
		genesisTransaction = new Transaction(coinbase.publicKey, this.billeteraUsuario.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		saldo = String.valueOf(billeteraUsuario.getBalance());
		
		this.billeteraAdmin.generateKeyPair();
		saldoAdmin = String.valueOf(billeteraAdmin.getBalance());
	}

	public void generateBuy(ActionEvent actionEvent) {
		float valorArbol = numeroArboles * 7f;
		Block block1 = new Block(genesis.hash);
		block1.addTransaction(billeteraUsuario.sendFunds(billeteraAdmin.publicKey, valorArbol));
		addBlock(block1);
		saldo = String.valueOf(billeteraUsuario.getBalance());
		saldoAdmin = String.valueOf(billeteraAdmin.getBalance());
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(0);
		blockchain.add(newBlock);
	}
	
	public Integer getNumeroArboles() {
		return numeroArboles;
	}

	public void setNumeroArboles(Integer numeroArboles) {
		this.numeroArboles = numeroArboles;
	}

	public Wallet getBilleteraUsuario() {
		return billeteraUsuario;
	}

	public void setBilleteraUsuario(Wallet billeteraUsuario) {
		this.billeteraUsuario = billeteraUsuario;
	}

	public Wallet getBilleteraAdmin() {
		return billeteraAdmin;
	}

	public void setBilleteraAdmin(Wallet billeteraAdmin) {
		this.billeteraAdmin = billeteraAdmin;
	}
	

	public Boolean getHabilitarLlave() {
		return habilitarLlave;
	}

	public void setHabilitarLlave(Boolean habilitarLlave) {
		this.habilitarLlave = habilitarLlave;
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

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getPublicKeyIn() {
		return publicKeyIn;
	}

	public void setPublicKeyIn(String publicKeyIn) {
		this.publicKeyIn = publicKeyIn;
	}

	public String getSaldoAdmin() {
		return saldoAdmin;
	}

	public void setSaldoAdmin(String saldoAdmin) {
		this.saldoAdmin = saldoAdmin;
	}
}
