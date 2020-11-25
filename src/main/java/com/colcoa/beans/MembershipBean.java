package com.colcoa.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.colcocoa.entities.Usuarios;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Clase que ejecuta las transacciones de Donaciones
 * 
 * @author Propietario
 *
 */
@ManagedBean(name = "membershipBean")
@ViewScoped
public class MembershipBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String clientId = "AX8DXrUnZDEHUhrF6hS-i0wtFbdN_YqEQTqi4aPMjTn3wiCip2yUIfJ2-XT4V7xJ_Gh_HG6-cIGba_r8";
	private String clientSecret = "EFi-bJ7V5jDxBUOk7jzDuVuK1oWAkEuPGyuYxZQY586CQQ21D5TmpplCb_qUiPg9jkGDYfh1JuhljXlk";
	
	private String valorDonacion;

	
	public void donar(String donacion) {
		
		
		FacesMessage msg = null;
		
		Usuarios usuario = (Usuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGEDIN_USER");
		
		APIContext context = new APIContext(clientId, clientSecret, "live");

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("https://plantreforestation.com/services/PayPalService/paypal");
		redirectUrls.setReturnUrl("https://plantreforestation.com/services/PayPalService/paypalDonation");
		
		// Set payment details
		Details details = new Details();
		details.setShipping("0");
		
		details.setTax("0");

		// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("USD");
		
		amount.setDetails(details);
		if(donacion.equals("bronze")) {
				amount.setTotal("35.0");
		}else if(donacion.equals("silver")) {
			amount.setTotal("175.0");
		}else if(donacion.equals("gold")) {
			amount.setTotal("350.0");	
		}else if(donacion.equals("platinum")){
			
			if(valorDonacion.equals("") || Integer.parseInt(valorDonacion) <= 100) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong value", "Enter a number value more than 100(one hundred) trees");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}else{
				Integer valorArboles = Integer.parseInt(valorDonacion);
				if(valorArboles >= 4999) {
					Double valorArbol = new Double("2.5");
					Double valorTotal = valorArboles * valorArbol;
					String valorTotalString = Double.toString(valorTotal);
					amount.setTotal(valorTotalString);
				}else {
					Double valorArbol = new Double("3.5");
					Double valorTotal = valorArboles * valorArbol;
					String valorTotalString = Double.toString(valorTotal);
					amount.setTotal(valorTotalString);	
				}
			}
			
		}

		// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("Donation");

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
	
	public String getValorDonacion() {
		return valorDonacion;
	}
	
	public void setValorDonacion(String valorDonacion) {
		this.valorDonacion = valorDonacion;
	}
}
