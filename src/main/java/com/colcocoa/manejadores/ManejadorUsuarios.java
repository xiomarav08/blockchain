package com.colcocoa.manejadores;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcocoa.entities.Usuarios;

@Stateless
public class ManejadorUsuarios {

	@PersistenceContext(unitName = "OracleDS")
	private EntityManager em;
	
	public Usuarios consultarUsuario() {
		Query query=em.createQuery("SELECT u FROM Usuarios u WHERE u.id=:id");
		query.setParameter("id", 1);
		Usuarios u=(Usuarios)query.getSingleResult();
		return u;
	}
}
