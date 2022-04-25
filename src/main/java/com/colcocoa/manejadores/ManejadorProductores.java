package com.colcocoa.manejadores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcoa.enums.EnumEspecies;
import com.colcoa.enums.EnumEstado;
import com.colcocoa.entities.Productores;

@Stateless
public class ManejadorProductores {
	
	@PersistenceContext(unitName="MySqlDS")
	private EntityManager em;
	
	public List<Productores> consultarProductores() {
		List<Productores> productores = new ArrayList<Productores>();
		try {
			Query query=em.createQuery("SELECT p FROM Productores p WHERE p.estado = :estado group by p.nombre");
			query.setParameter("estado", EnumEstado.ACTIVO);
			productores = (List<Productores>)query.getResultList();
			return productores;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public List<Productores> consultarProductoresNombre(String nombre) {
		List<Productores> productores = new ArrayList<Productores>();
		try {
			Query query=em.createQuery("SELECT p FROM Productores p WHERE p.nombre = :nombre AND p.estado = :estado");
			query.setParameter("estado", EnumEstado.ACTIVO);
			query.setParameter("nombre", nombre);
			productores = (List<Productores>)query.getResultList();
			return productores;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Productores consultarProductoresPorEspecie(String productor, EnumEspecies especieBusqueda) {
		Productores productorRetorno = new Productores();
		try {
			if(EnumEspecies.TODAS.equals(especieBusqueda)) {
				Query query=em.createQuery("SELECT p FROM Productores p WHERE p.nombre = :nombre AND p.estado = :estado group by p.nombre");
				query.setParameter("estado", EnumEstado.ACTIVO);
				query.setParameter("nombre", productor);
				productorRetorno = (Productores)query.getSingleResult();
				return productorRetorno;
			}else {
				Query query=em.createQuery("SELECT p FROM Productores p WHERE p.nombre = :nombre AND p.nombreEspecie = :especie AND p.estado = :estado");
				query.setParameter("estado", EnumEstado.ACTIVO);
				query.setParameter("nombre", productor);
				query.setParameter("especie", especieBusqueda);
				productorRetorno = (Productores)query.getSingleResult();
				return productorRetorno;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
