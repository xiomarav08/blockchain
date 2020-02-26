package com.colcocoa.manejadores;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.colcoa.enums.EnumEstado;
import com.colcocoa.entities.Usuarios;

@Stateless
public class ManejadorUsuarios {

	@PersistenceContext(unitName="MySqlDS")
	private EntityManager em;
	
	public Usuarios consultarUsuario(String usuario) {
		Query query=em.createQuery("SELECT u FROM Usuarios u WHERE u.usuario = :usuario and u.estado = :estado");
		try {
			query.setParameter("estado", EnumEstado.ACTIVO);
			query.setParameter("usuario", usuario);
			Usuarios u=(Usuarios)query.getSingleResult();
			return u;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Usuarios consultarUsuarioPorEmail(String email) {
		Query query=em.createQuery("SELECT u FROM Usuarios u WHERE u.email = :email and u.estado = :estado");
		try {
			query.setParameter("estado", EnumEstado.ACTIVO);
			query.setParameter("email", email);
			Usuarios u=(Usuarios)query.getSingleResult();
			return u;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	public List<Usuarios> findAll() {
		try {
			Query query=em.createQuery("SELECT u FROM Usuarios u WHERE u.estado = :estado AND u.usuario <> :usuarioAdmin");
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("blockchain.properties");
			prop.load(inputStream);
			
			String usuarioAdmin = prop.getProperty("usuario");
			query.setParameter("usuarioAdmin", usuarioAdmin);
			query.setParameter("estado", EnumEstado.ACTIVO);
		
			List<Usuarios> listaUsuarios=(List<Usuarios>)query.getResultList();
			
			return listaUsuarios;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public void updateUser(Usuarios user){
		Query query=em.createQuery("UPDATE Usuarios u SET u.token = :token WHERE u.id = :id");
		try {
			query.setParameter("token", user.getToken());
			query.setParameter("id", user.getId());
			query.executeUpdate();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updatePassword(Usuarios user, String password){
		Query query=em.createQuery("UPDATE Usuarios u SET u.clave = :clave WHERE u.id = :id");
		try {
			query.setParameter("clave", password);
			query.setParameter("id", user.getId());
			query.executeUpdate();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Boolean almacenarUsuario(Usuarios usuario) {
		usuario.setEstado(EnumEstado.ACTIVO);
		try {
			em.persist(usuario);
			return Boolean.TRUE;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return Boolean.FALSE;
		}
	}
	
	public Boolean deleteUser(Usuarios usuario) {
		usuario.setEstado(EnumEstado.INACTIVO);
		try {
			em.merge(usuario);
			return Boolean.TRUE;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return Boolean.FALSE;
		}
	}
}
