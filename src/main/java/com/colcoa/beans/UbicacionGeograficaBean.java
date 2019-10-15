package com.colcoa.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.colcocoa.entities.UbicacionGeograficaEntity;


@ManagedBean(name="ubicacionGeografica")
@SessionScoped
public class UbicacionGeograficaBean {
	
	private MapModel simpleModel;
	  
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
    }
  
    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void inicializarPuntos(List<UbicacionGeograficaEntity> listaUbicacionGeografica) {
    	simpleModel = new DefaultMapModel();
    	for(UbicacionGeograficaEntity ug: listaUbicacionGeografica) {
    		Double ubicacionX = new Double(ug.getUbicacionX().replace(",", "."));
    		Double ubicacionY = new Double(ug.getUbicacionY().replace(",", "."));
    		LatLng coord = new LatLng(ubicacionX, ubicacionY);
			simpleModel.addOverlay(new Marker(coord, "Hacienda la tentacion"));
    	}
    }
}
