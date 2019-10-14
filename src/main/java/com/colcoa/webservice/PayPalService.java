package com.colcoa.webservice;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Path("/PayPalService")
@Consumes({ "application/json" })
public class PayPalService {

	private String clientId = "AXDW5dSHuoi9VgvVE9tknTFgZuUgmUcltO_YMfdLsLIJVS29yKbN0ehJoda-1bi28xK8Q9SanFORFBjP";
	private String clientSecret = "EOjnbliyXupszBDEeILoS_qGLl0PNtqvHQYwt-Svzf9_WgQSODMRblQ6V05GmrSy6gd8ayLabgLSE8_R";
	
	@GET
	@Path("/paypal")
	public void paypal(@QueryParam("paymentId") String paymentID, @QueryParam("token") String token,
			@QueryParam("PayerID") String PayerID) {
		
		APIContext context = new APIContext(clientId, clientSecret, "sandbox");
		
		try {
			
			Payment payment = new Payment();
			payment.setId(paymentID);
			

			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(PayerID);
			
			Payment paymentFinish =payment.execute(context, paymentExecution);
			if(paymentFinish.getState().equals("approved")) {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				ec.redirect(ec.getRequestContextPath() + "/billetera.xhtml");
			}
		} catch (PayPalRESTException ex) {
			System.err.println(ex.getDetails());
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
