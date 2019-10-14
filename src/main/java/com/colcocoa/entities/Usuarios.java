package com.colcocoa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.colcoa.enums.EnumTipoDocumento;
import com.colcoa.enums.EnumTipoPersona;

@Entity(name = "Usuarios")
@Table(name="USUARIOS")
public class Usuarios extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USUARIO_ID_GENERATOR")
	@GenericGenerator(name = "USUARIO_ID_GENERATOR", strategy = "native")
	private Integer id;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "APELLIDOS")
	private String apellidos;
	
	@Column(name = "NUMERODEIDENTIFICACION")
	private String numeroDeIdentificacion;
	
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE_USER")
	private EnumTipoPersona tipoPersona;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE_DOCUMENT")
	private EnumTipoDocumento tipoDocumento;
	
	
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
	
	public String getNumeroDeIdentificacion() {
		return numeroDeIdentificacion;
	}
	
	public void setNumeroDeIdentificacion(String numeroDeIdentificacion) {
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
	
	public EnumTipoPersona getTipoPersona() {
		return tipoPersona;
	}
	
	public void setTipoPersona(EnumTipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	public EnumTipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	
	public void setTipoDocumento(EnumTipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuarios other = (Usuarios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
