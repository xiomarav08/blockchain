package com.colcoa.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "plantarBean")
@SessionScoped
public class PlantarBean {

	private List<String> arboles;

	private String clientId = "AXDW5dSHuoi9VgvVE9tknTFgZuUgmUcltO_YMfdLsLIJVS29yKbN0ehJoda-1bi28xK8Q9SanFORFBjP";
	private String clientSecret = "EOjnbliyXupszBDEeILoS_qGLl0PNtqvHQYwt-Svzf9_WgQSODMRblQ6V05GmrSy6gd8ayLabgLSE8_R";
	
	private Integer numeroArboles;
	
	private String arbol;

	@PostConstruct
	public void Init() {
		arboles = Arrays.asList("Abarco", "Caoba", "Moncoro", "Cacao");
	}

	
	public void comprar() {
		
		APIContext context = new APIContext(clientId, clientSecret, "sandbox");

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Set redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://127.0.0.1:8080/BlockChain-0.0.1-SNAPSHOT/services/PayPalService/paypal");
		redirectUrls.setReturnUrl("http://127.0.0.1:8080/BlockChain-0.0.1-SNAPSHOT/services/PayPalService/paypal");
		
		// Set payment details
		Details details = new Details();
		details.setShipping("0");
		
		details.setTax("0");

		// Payment amount
		Amount amount = new Amount();
		amount.setCurrency("USD");
		
		amount.setDetails(details);
		if(this.getArbol().equals("Abarco") || this.getArbol().equals("Caoba") || this.getArbol().equals("Moncoro")) {
			if(numeroArboles <= 999) {
				Double value = new Double("3.0");
				value = value * numeroArboles;
				details.setSubtotal(value.toString());
				// Total must be equal to sum of shipping, tax and subtotal.
				amount.setTotal(value.toString());
			}else if(numeroArboles >= 999 && numeroArboles <= 9999) {
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
			Double value = new Double("1.75");
			value = value * numeroArboles;
			details.setSubtotal(value.toString());
			// Total must be equal to sum of shipping, tax and subtotal.
			amount.setTotal(value.toString());
		}

		

		// Transaction information
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment transaction description.");

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
}
