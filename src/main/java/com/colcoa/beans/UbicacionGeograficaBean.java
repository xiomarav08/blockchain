package com.colcoa.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.colcocoa.entities.UbicacionGeograficaEntity;


@ManagedBean(name="ubicacionGeografica")
public class UbicacionGeograficaBean{
	
	private static final long serialVersionUID = 1L;
	
	private MapModel simpleModel;
	
	private List<UbicacionGeograficaEntity> listUbicacionGeografica;
	  
    @PostConstruct
    public void init() {
    	if(FacesContext.getCurrentInstance() != null) {
    		 
    		List<UbicacionGeograficaEntity> listaUbicacionGeografica = (List<UbicacionGeograficaEntity>)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("PUNTOS");
    		
    		if(listaUbicacionGeografica != null) {
    			inicializarPuntos(listaUbicacionGeografica);
    		}
    	}
    }
  
    public void inicializarPuntos(List<UbicacionGeograficaEntity> listaUbicacionGeografica) {
    	MapModel simpleModel = new DefaultMapModel();
    	for(UbicacionGeograficaEntity ug: listaUbicacionGeografica) {
    		Double ubicacionX = new Double(ug.getUbicacionX().replace(",", "."));
    		Double ubicacionY = new Double(ug.getUbicacionY().replace(",", "."));
    		LatLng coord = new LatLng(ubicacionX, ubicacionY);
			simpleModel.addOverlay(new Marker(coord, "Hacienda la tentacion"));
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
    
    public List<UbicacionGeograficaEntity> getListUbicacionGeografica() {
		return listUbicacionGeografica;
	}
    
    public void setListUbicacionGeografica(List<UbicacionGeograficaEntity> listUbicacionGeografica) {
		this.listUbicacionGeografica = listUbicacionGeografica;
	}
}
