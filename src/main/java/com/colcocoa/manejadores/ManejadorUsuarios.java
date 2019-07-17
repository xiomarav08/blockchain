package com.colcocoa.manejadores;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcocoa.entities.Usuarios;

@Stateless
public class ManejadorUsuarios {

	@PersistenceContext(unitName="OracleDS")
	private EntityManager em;
	
	public Usuarios consultarUsuario(String usuario) {
		Query query=em.createQuery("SELECT u FROM Usuarios u WHERE u.usuario = :usuario");
		try {
			query.setParameter("usuario", usuario);
			Usuarios u=(Usuarios)query.getSingleResult();
			return u;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	public Boolean almacenarUsuario(Usuarios usuario) {
		try {
			em.persist(usuario);
			return Boolean.TRUE;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return Boolean.FALSE;
		}
	}
}
