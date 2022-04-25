package com.colcocoa.manejadores;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcoa.enums.EnumTipoArbol;
import com.colcocoa.entities.Productores;
import com.colcocoa.entities.TransactionEntity;
import com.colcocoa.entities.UbicacionGeograficaEntity;

@Stateless
public class ManejadorUbicacionGeografica {
	

	@PersistenceContext(unitName="MySqlDS")
	private EntityManager em;
	
	
	public List<UbicacionGeograficaEntity> consultarUbicacion(Integer numeroArboles, EnumTipoArbol tipoArbol) {
		try {
			Query query=em.createQuery("SELECT u FROM UbicacionGeografica u WHERE u.tipoArbol = :tipoArbol and u.ocupado = :ocupado");
			query.setParameter("tipoArbol", tipoArbol);
			query.setParameter("ocupado", Boolean.FALSE);
			query.setMaxResults(numeroArboles);
			List<UbicacionGeograficaEntity> listUbicacionGeografica =(List<UbicacionGeograficaEntity>)query.getResultList();
			return listUbicacionGeografica;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public List<UbicacionGeograficaEntity> consultarUbicacionProductor(Productores productores) {
		try {
			Query query=em.createQuery("SELECT u FROM UbicacionGeografica u WHERE u.productor = :productores");
			query.setParameter("productores", productores);
			List<UbicacionGeograficaEntity> listUbicacionGeografica =(List<UbicacionGeograficaEntity>)query.getResultList();
			return listUbicacionGeografica;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public List<UbicacionGeograficaEntity> consultarUbicacionPorTransacion(TransactionEntity transactionEntity) {
		try {
			Query query=em.createQuery("SELECT u FROM UbicacionGeografica u WHERE u.transaction = :transaction");
			query.setParameter("transaction", transactionEntity);
			List<UbicacionGeograficaEntity> listUbicacionGeografica =(List<UbicacionGeograficaEntity>)query.getResultList();
			return listUbicacionGeografica;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public void actualizarArboles(List<UbicacionGeograficaEntity> listUbicacionGeografica) {
		for(UbicacionGeograficaEntity ubicacionGeografica: listUbicacionGeografica) {
			actualizarArbol(ubicacionGeografica);
		}
	}
	
	public Boolean actualizarArbol(UbicacionGeograficaEntity ubicacionGeografica) {
		try {
			em.merge(ubicacionGeografica);
			return Boolean.TRUE;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return Boolean.FALSE;
		}
	}

}
