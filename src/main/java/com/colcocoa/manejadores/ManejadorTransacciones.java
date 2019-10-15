package com.colcocoa.manejadores;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.Usuarios;

@Stateless
public class ManejadorTransacciones {
	
	@PersistenceContext(unitName="MySqlDS")
	private EntityManager em;

	public List<TransactionEntity> consultarTransaccionesPorUsuario(Usuarios usuario) {
		try {
			Query query=em.createQuery("SELECT t FROM TransactionEntity t WHERE t.userSender = :usuario OR t.userRecipient = :usuario");
			query.setParameter("usuario", usuario);
			List<TransactionEntity> listTransaction =(List<TransactionEntity>)query.getResultList();
			return listTransaction;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Integer consultarUltimaTransaccion() {
		try {
			Query query= em.createQuery("SELECT max(t.id) FROM TransactionEntity t");
			Integer id = (Integer)query.getSingleResult();
			return id;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public TransactionEntity obtenerTransaccionPorId(Integer id) {
		try {
			Query query= em.createQuery("SELECT t FROM TransactionEntity t WHERE t.id = :id");
			query.setParameter("id", id);
			TransactionEntity transactionEntity = (TransactionEntity)query.getSingleResult();
			return transactionEntity;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public TransactionEntity almacenarTransaccion(TransactionEntity transactionEntity) {
		try {
			em.persist(transactionEntity);
			return transactionEntity;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
