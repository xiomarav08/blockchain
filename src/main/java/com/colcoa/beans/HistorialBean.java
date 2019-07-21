package com.colcoa.beans;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.colcoa.dto.SaldoDTO;
import com.colcocoa.entities.Transaction;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "historialBean")
public class HistorialBean {
	
	private SaldoDTO saldo;
	
	private final String USUARIO_ADMIN = "admin";
	
	private List<Transaction> listTransactions;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	
	@PostConstruct
	private void Init() {
		saldo = new SaldoDTO();
		Usuarios usuario = manejadorUsuarios.consultarUsuario(USUARIO_ADMIN);
		saldo.setSaldoTotal(usuario.getBalance().toString());
		saldo.setNit(usuario.getNumeroDeIdentificacion());
		saldo.setNombre(usuario.getNombre() + " "+ usuario.getApellidos());
		saldo.setFechaIngreso(new Date());
		listTransactions = manejadorTransacciones.consultarTransaccionesPorUsuario(usuario);
	}

	public SaldoDTO getSaldo() {
		return saldo;
	}

	public void setSaldo(SaldoDTO saldo) {
		this.saldo = saldo;
	}


	public List<Transaction> getListTransactions() {
		return listTransactions;
	}


	public void setListTransactions(List<Transaction> listTransactions) {
		this.listTransactions = listTransactions;
	}
	
	
}
