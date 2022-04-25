package com.colcoa.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.colcoa.enums.EnumEspecies;
import com.colcocoa.entities.Productores;
import com.colcocoa.entities.UbicacionGeograficaEntity;
import com.colcocoa.manejadores.ManejadorProductores;
import com.colcocoa.manejadores.ManejadorUbicacionGeografica;

@ManagedBean(name = "secoBean")
@ViewScoped
public class SecoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private List<String> productores;
	
	private String productorBusqueda;
	
	private Productores productor;
	
	private EnumEspecies especieBusqueda;
	
	private MapModel simpleModel;
	
	@Inject
	private ManejadorProductores manejadorProductores;
	
	@Inject
	private ManejadorUbicacionGeografica manejadorUbicacionGeografica;
	
	@PostConstruct
	public void Init() {
		productores = new ArrayList<String>();
		List<Productores> productoresEntity = manejadorProductores.consultarProductores();
		for(Productores prod : productoresEntity) {
			productores.add(prod.getNombre());
		}
	}
	
	
	public void busquedaProductores() {
		if(especieBusqueda == null) {
			especieBusqueda = EnumEspecies.TODAS;
		}
		productor = manejadorProductores.consultarProductoresPorEspecie(productorBusqueda, especieBusqueda);
		List<UbicacionGeograficaEntity> listaUbicacionGeografica = new ArrayList<UbicacionGeograficaEntity>();
		if(EnumEspecies.TODAS.equals(especieBusqueda)) {
			List<Productores> productoresEntity = manejadorProductores.consultarProductoresNombre(productorBusqueda);
			for(Productores prod :productoresEntity) {
				listaUbicacionGeografica.addAll(manejadorUbicacionGeografica.consultarUbicacionProductor(prod));
			}
		}else {
			listaUbicacionGeografica = manejadorUbicacionGeografica.consultarUbicacionProductor(productor);
		}
		inicializarPuntos(listaUbicacionGeografica);
		
	}
	
	public void inicializarPuntos(List<UbicacionGeograficaEntity> listaUbicacionGeografica) {
    	MapModel simpleModel = new DefaultMapModel();
    	for(UbicacionGeograficaEntity ug: listaUbicacionGeografica) {
    		Double ubicacionX = new Double(ug.getUbicacionX().replace(",", "."));
    		Double ubicacionY = new Double(ug.getUbicacionY().replace(",", "."));
    		LatLng coord = new LatLng(ubicacionX, ubicacionY);
    		Marker marker = new Marker(coord, ug.getProductor().getNombreEspecie().getMessage());
    		//marker.setIcon("/imagenes/abarcoMapa.png");
			simpleModel.addOverlay(marker);
    	}
    	if(FacesContext.getCurrentInstance().getExternalContext() != null) {
    		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MODEL", simpleModel);
    	}
    	
    }
	
	public MapModel getSimpleModel() {
    	if(simpleModel != null) {
    		return simpleModel;
    	}else if(FacesContext.getCurrentInstance() != null){
    		simpleModel = (MapModel) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("MODEL");
    		return simpleModel;
    	}
    	return null;
    }
    
    public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}
	
	public List<String> getProductores() {
		return productores;
	}
	
	public void setProductores(List<String> productores) {
		this.productores = productores;
	}
	
	public String getProductorBusqueda() {
		return productorBusqueda;
	}

	public void setProductorBusqueda(String productorBusqueda) {
		this.productorBusqueda = productorBusqueda;
	}

	public Productores getProductor() {
		return productor;
	}

	public void setProductor(Productores productor) {
		this.productor = productor;
	}

	public EnumEspecies getEspecieBusqueda() {
		return especieBusqueda;
	}

	public void setEspecieBusqueda(EnumEspecies especieBusqueda) {
		this.especieBusqueda = especieBusqueda;
	}

	public EnumEspecies[] getEspecies() {
        return EnumEspecies.values();
    }

}
