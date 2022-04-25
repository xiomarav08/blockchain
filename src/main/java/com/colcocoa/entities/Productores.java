package com.colcocoa.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.colcoa.enums.EnumEspecies;

@Entity(name = "Productores")
@Table(name="PRODUCTORES")
public class Productores extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRODUCTORES_ID_GENERATOR")
	@GenericGenerator(name = "PRODUCTORES_ID_GENERATOR", strategy = "native")
	private Integer id;
	
	@Column(name = "NOMBREPREDIO")
	private String nombrePredio;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "MUNICIPIO")
	private String municipio;
	
	@Column(name = "DEPARTAMENTO")
	private String departamento;
	
	@Column(name = "VEREDA")
	private String vereda;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "NOMBRE_ESPECIE")
	private EnumEspecies nombreEspecie;
	
	@Column(name = "HECTAREAS")
	private Double hectareas;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHASIEMBRA")
	private Date fechaSiembra;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombrePredio() {
		return nombrePredio;
	}

	public void setNombrePredio(String nombrePredio) {
		this.nombrePredio = nombrePredio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getVereda() {
		return vereda;
	}

	public void setVereda(String vereda) {
		this.vereda = vereda;
	}

	public EnumEspecies getNombreEspecie() {
		return nombreEspecie;
	}

	public void setNombreEspecie(EnumEspecies nombreEspecie) {
		this.nombreEspecie = nombreEspecie;
	}

	public Double getHectareas() {
		return hectareas;
	}

	public void setHectareas(Double hectareas) {
		this.hectareas = hectareas;
	}

	public Date getFechaSiembra() {
		return fechaSiembra;
	}

	public void setFechaSiembra(Date fechaSiembra) {
		this.fechaSiembra = fechaSiembra;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		Productores other = (Productores) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
