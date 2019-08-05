package com.colcoa.beans;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.colcoa.dto.SaldoDTO;
import com.colcoa.enums.EnumTipoTransacion;
import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
import com.colcocoa.manejadores.ManejadorUsuarios;

@ManagedBean(name = "historialBean")
public class HistorialBean {
	
	private SaldoDTO saldo;
	
	private final String USUARIO_ADMIN = "admin";
	
	private List<TransactionEntity> listTransactions;
	
	private Usuarios usuarioAdmin;
	
	@Inject
	private ManejadorUsuarios manejadorUsuarios;
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	
	@PostConstruct
	private void Init() {
		saldo = new SaldoDTO();
		usuarioAdmin = manejadorUsuarios.consultarUsuario(USUARIO_ADMIN);
		saldo.setNit(usuarioAdmin.getNumeroDeIdentificacion());
		saldo.setNombre(usuarioAdmin.getNombre() + " "+ usuarioAdmin.getApellidos());
		saldo.setFechaIngreso(new Date());
		listTransactions = manejadorTransacciones.consultarTransaccionesPorUsuario(usuarioAdmin);
	}
	
	public Integer calcularSaldoAdmin() {
		Float saldoTotal = 0f;
		for (TransactionEntity transactionEntity : listTransactions) {
			if(EnumTipoTransacion.COMPRA.equals(transactionEntity.getTipoTransacion())) {
				saldoTotal = saldoTotal + transactionEntity.getValue();
			}else if(EnumTipoTransacion.RECARGA.equals(transactionEntity.getTipoTransacion())){
				saldoTotal = saldoTotal - transactionEntity.getValue();
			}
		}
		return Math.round(saldoTotal) * -1;
	}

	public SaldoDTO getSaldo() {
		return saldo;
	}

	public void setSaldo(SaldoDTO saldo) {
		this.saldo = saldo;
	}


	public List<TransactionEntity> getListTransactions() {
		return listTransactions;
	}


	public void setListTransactions(List<TransactionEntity> listTransactions) {
		this.listTransactions = listTransactions;
	}
	
	
}
