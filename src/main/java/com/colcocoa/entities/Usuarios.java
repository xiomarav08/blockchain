package com.colcocoa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Usuarios")
@Table(name = "USUARIOS")
public class Usuarios implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "APELLIDOS")
	private String apellidos;
	
	@Column(name = "NUMERODEIDENTIFICACION")
	private Integer numeroDeIdentificacion;
	
	@Column(name = "TELEFONO")
	private String telefono;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "USUARIO")
	private String usuario;
	
	@Column(name = "CLAVE")
	private String clave;
	
	@Column(name = "PUBLICKEY")
	private String publicKey;
	
	@Column(name = "PRIVATEKEY")
	private String privateKey;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Integer getNumeroDeIdentificacion() {
		return numeroDeIdentificacion;
	}
	public void setNumeroDeIdentificacion(Integer numeroDeIdentificacion) {
		this.numeroDeIdentificacion = numeroDeIdentificacion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
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
	
	
	
	

}
