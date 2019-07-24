package com.colcocoa.manejadores;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcocoa.entities.Transaction;
import com.colcocoa.entities.Usuarios;

@Stateless
public class ManejadorTransacciones {
	
	@PersistenceContext(unitName="OracleDS")
	private EntityManager em;

	public List<Transaction> consultarTransaccionesPorUsuario(Usuarios usuario) {
		try {
			Query query=em.createQuery("SELECT u FROM Transaction t WHERE t.userSender = :usuario");
			query.setParameter("usuario", usuario);
			List<Transaction> listTransaction =(List<Transaction>)query.getResultList();
			return listTransaction;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}