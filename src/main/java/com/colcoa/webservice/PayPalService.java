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

	private String clientId = "AXDW5dSHuoi9VgvVE9tknTFgZuUgmUcltO_YMfdLsLIJVS29yKbN0ehJoda-1bi28xK8Q9SanFORFBjP";
	private String clientSecret = "ECKV4lp2X0SBrxoK61DYPvcplXYdvCip1DuoMShcRowkV4mNCGqOCN36ABExNFMHKCA96txUKBOwm90N";
	
	@Inject
	private TransactionBean transaction;
	
	@GET
	@Path("/paypal")
	public Response paypal(@QueryParam("treeAmmount") String numeroArboles, @QueryParam("usuario") String usuario,  @QueryParam("paymentId") String paymentID, @QueryParam("token") String token,
			@QueryParam("PayerID") String PayerID) {
		
		APIContext context = new APIContext(clientId, clientSecret, "sandbox");
		
		try {
			
			Payment payment = new Payment();
			payment.setId(paymentID);
			

			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(PayerID);
			
			Payment paymentFinish =payment.execute(context, paymentExecution);
			if(paymentFinish.getState().equals("approved")) {
				URI url = new URI("http://127.0.0.1:8080/BlockChain-0.0.1-SNAPSHOT/billetera.xhtml");
				
				Integer numeroArbolesInt = Integer.parseInt(numeroArboles);
				String arbol = paymentFinish.getTransactions().get(0).getDescription();
				String valor = paymentFinish.getTransactions().get(0).getAmount().getTotal();
				
				if(arbol.equals("Cacao")) {
					transaction.comprarArbol(numeroArbolesInt, EnumTipoArbol.CACACO, valor, usuario);
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
