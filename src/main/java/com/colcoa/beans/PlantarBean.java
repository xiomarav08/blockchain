package com.colcoa.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.colcocoa.entities.Usuarios;
import com.colcocoa.manejadores.ManejadorTransacciones;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "plantarBean")
@ViewScoped
public class PlantarBean{
	
	private List<String> arboles;

	private String clientId = "AX8DXrUnZDEHUhrF6hS-i0wtFbdN_YqEQTqi4aPMjTn3wiCip2yUIfJ2-XT4V7xJ_Gh_HG6-cIGba_r8";
	private String clientSecret = "EFi-bJ7V5jDxBUOk7jzDuVuK1oWAkEuPGyuYxZQY586CQQ21D5TmpplCb_qUiPg9jkGDYfh1JuhljXlk";
	
	private Integer numeroArboles;
	
	private String arbol;
	
	private List<String> imagesSlide;
	
	private Integer arbolesPlantados;
	
	private String nombreUsuario;
	
	@Inject
	private ManejadorTransacciones manejadorTransacciones;
	
	@PostConstruct
	public void Init() {
		//lista de arboles que se necesitaran en un futuro
		//arboles = Arrays.asList("Abarco", "Caoba", "Moncoro", "Cacao");
		arboles = Arrays.asList("Your Trees");
		imagesSlide = Arrays.asList("why-planT.png","Forest-Species.png","Species.png");
		Usuarios usuario = (Usuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGEDIN_USER");
		nombreUsuario = usuario.getNombre();
		arbolesPlantados = Math.round(manejadorTransacciones.obtenerArbolesPlantados(usuario));
	}



	public void comprar() {
		
		Usuarios usuario = (Usuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGEDIN_USER");
		
		APIContext context = new APIContext(clientId, clientSecret, "live");

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("https://www.plantreforestation.com/services/PayPalService/paypal");
		redirectUrls.setReturnUrl("https://www.plantreforestation.com/services/PayPalService/paypal"+"?treeAmmount="+numeroArboles+"&usuario="+usuario.getUsuario());
		
		// Set payment details
		Details details = new Details();
		details.setShipping("0");
		
		details.setTax("0");

		// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("USD");
		
		amount.setDetails(details);
		if(this.getArbol().equals("Your Trees")) {
//			if(usuario.getId().equals(22)) {
//				if(numeroArboles <= 999) {
//					Double value = new Double("3.5");
//					value = value * numeroArboles;
//					details.setSubtotal(value.toString());
//					// Total must be equal to sum of shipping, tax and subtotal.
//					amount.setTotal(value.toString());
//				}else if(numeroArboles >= 1000) {
//					Double value = new Double("0.001");
//					value = value * numeroArboles;
//					details.setSubtotal(value.toString());
//					// Total must be equal to sum of shipping, tax and subtotal.
//					amount.setTotal(value.toString());
//				}
			if(numeroArboles <= 4999) {
				Double value = new Double("3.5");
				value = value * numeroArboles;
				details.setSubtotal(value.toString());
				// Total must be equal to sum of shipping, tax and subtotal.
				amount.setTotal(value.toString());
			}else if(numeroArboles >= 4999 && numeroArboles <= 9999) {
				Double value = new Double("2.75");
				value = value * numeroArboles;
				details.setSubtotal(value.toString());
				// Total must be equal to sum of shipping, tax and subtotal.
				amount.setTotal(value.toString());
			}else if(numeroArboles >= 9999) {
				Double value = new Double("1.75");
				value = value * numeroArboles;
				details.setSubtotal(value.toString());
				// Total must be equal to sum of shipping, tax and subtotal.
				amount.setTotal(value.toString());
			}			
		}else {
			Double value = new Double("3.0");
			value = value * numeroArboles;
			details.setSubtotal(value.toString());
			// Total must be equal to sum of shipping, tax and subtotal.
			amount.setTotal(value.toString());
		}


		// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription(arbol);

		// Add transaction to a list
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		// Add payment details
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);

		try {
			Payment createdPayment = payment.create(context);

			Iterator links = createdPayment.getLinks().iterator();
			while (links.hasNext()) {
				Links link = (Links) links.next();
				if (link.getRel().equalsIgnoreCase("approval_url")) {
					// Redirect the customer to link.getHref()
					ExternalContext facesContext = FacesContext.getCurrentInstance().getExternalContext();
					facesContext.redirect(link.getHref());
				}
			}
		} catch (PayPalRESTException ex) {
			System.err.println(ex.getDetails());
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}

	}
	
	public void escogerArbol(String arbol) {
		this.setArbol(arbol);
	}
	
	public Integer getNumeroArboles() {
		return numeroArboles;
	}
	
	public void setNumeroArboles(Integer numeroArboles) {
		this.numeroArboles = numeroArboles;
	}
	
	public String getArbol() {
		return arbol;
	}
	
	public void setArbol(String arbol) {
		this.arbol = arbol;
	}
	
	public List<String> getArboles() {
		return arboles;
	}

	public void setArboles(List<String> arboles) {
		this.arboles = arboles;
	}
	
	public List<String> getImagesSlide() {
		return imagesSlide;
	}
	
	public void setImagesSlide(List<String> imagesSlide) {
		this.imagesSlide = imagesSlide;
	}
	
	public Integer getArbolesPlantados() {
		return arbolesPlantados;
	}
	
	public void setArbolesPlantados(Integer arbolesPlantados) {
		this.arbolesPlantados = arbolesPlantados;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
}
