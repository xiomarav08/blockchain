package com.colcoa.webservice;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.colcoa.beans.TransactionBean;
import com.colcoa.enums.EnumTipoArbol;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Path("/PayPalService")
@Consumes({ "application/json" })
public class PayPalService {

	private String clientId = "AX8DXrUnZDEHUhrF6hS-i0wtFbdN_YqEQTqi4aPMjTn3wiCip2yUIfJ2-XT4V7xJ_Gh_HG6-cIGba_r8";
	private String clientSecret = "EFi-bJ7V5jDxBUOk7jzDuVuK1oWAkEuPGyuYxZQY586CQQ21D5TmpplCb_qUiPg9jkGDYfh1JuhljXlk";
	
	@Inject
	private TransactionBean transaction;
	
	@GET
	@Path("/paypal")
	public Response paypal(@QueryParam("treeAmmount") String numeroArboles, @QueryParam("usuario") String usuario,  @QueryParam("paymentId") String paymentID, @QueryParam("token") String token,
			@QueryParam("PayerID") String PayerID) {
		
		APIContext context = new APIContext(clientId, clientSecret, "live");
		
		try {
			
			Payment payment = new Payment();
			payment.setId(paymentID);
			

			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(PayerID);
			
			Payment paymentFinish =payment.execute(context, paymentExecution);
			if(paymentFinish.getState().equals("approved")) {
				URI url = new URI("https://plantreforestation.com/billetera.xhtml");
				
				Integer numeroArbolesInt = Integer.parseInt(numeroArboles);
				String arbol = paymentFinish.getTransactions().get(0).getDescription();
				String valor = paymentFinish.getTransactions().get(0).getAmount().getTotal();
				
				if(arbol.equals("Cacao")) {
					transaction.comprarArbol(numeroArbolesInt, EnumTipoArbol.CACAO, valor, usuario);
				}else {
					transaction.comprarArbol(numeroArbolesInt, EnumTipoArbol.FORESTAL, valor, usuario);
				}
				
				return Response.temporaryRedirect(url).build();
			}
			
			
			return null;
		} catch (PayPalRESTException ex) {
			System.err.println(ex.getDetails());
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}

}
